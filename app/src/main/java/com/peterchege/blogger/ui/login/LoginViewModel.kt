package com.peterchege.blogger.ui.login

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.peterchege.blogger.api.requests.LoginUser
import com.peterchege.blogger.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val repository: LoginRepository,
    private val savedStateHandle: SavedStateHandle,
    private val sharedPreferences: SharedPreferences
):ViewModel() {

    private var _state = mutableStateOf(LoginScreenState())
    var state : State<LoginScreenState> = _state

    private var _isLoading = mutableStateOf(false)
    var isLoading:State<Boolean> = _isLoading

    private var _usernameState = mutableStateOf(TextFieldState())
    var usernameState: State<TextFieldState> = _usernameState

    private var _passwordState = mutableStateOf(TextFieldState())
    var passwordState: State<TextFieldState> =  _passwordState

    private var _LoginResponseState = mutableStateOf(LoginResponse())
    var LoginResponseState: State<LoginResponse> =  _LoginResponseState

    private val _passwordVisibility = mutableStateOf(true)
    var passwordVisibility: State<Boolean> =  _passwordVisibility

    fun onChangePasswordVisibility(){
        _passwordVisibility.value = !_passwordVisibility.value
    }

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    data class LoginScreenState(
        val isLoading: Boolean = false,

        val msg:String = "",
        val success:Boolean= false,
//        val loginResponse: LoginResponse= LoginResponse("",false),

    )
    data class LoginResponse(
        val msg: String = "",
        val success:Boolean = false
    )
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }


    fun OnChangeUsername(text:String){
        _usernameState.value = TextFieldState(text = text)


    }
    fun OnChangePassword(text:String){
        _passwordState.value = TextFieldState(text = text)

    }

    fun initiateLogin(navController: NavController,scaffoldState: ScaffoldState,context: Context){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcm error", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            viewModelScope.launch {
                _isLoading.value = true
                if (_usernameState.value.text.length < 5 || _passwordState.value.text.length< 5){
                    _isLoading.value = false
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "Credentials should be atleast 5 characters long",
                    )
                }else{
                    if(hasInternetConnection(context)){
                        val loginUser = LoginUser(_usernameState.value.text.trim(),_passwordState.value.text.trim(), token = token!!)
                        try {
                            val response = repository.loginUser(loginUser)
                            _isLoading.value = false
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = response.msg,
                            )
                            if (response.success){
                                val userInfoEditor = sharedPreferences.edit()
                                userInfoEditor.apply{
                                    putString(Constants.LOGIN_USERNAME,response.user?.username)
                                    putString(Constants.LOGIN_PASSWORD, response.user?.password)
                                    putString(Constants.LOGIN_FULLNAME, response.user?.fullname)
                                    putString(Constants.LOGIN_IMAGEURL, response.user?.imageUrl)
                                    putString(Constants.LOGIN_EMAIL,response.user?.email)
                                    putString(Constants.LOGIN_ID, response.user?._id)
                                    putString(Constants.FCM_TOKEN,token)
                                    apply()
                                }
                                navController.navigate(Screens.DASHBOARD_SCREEN)
                            }
                        }catch (e:HttpException){
                            _isLoading.value = false
                            Log.e("http error",e.localizedMessage?:"Server error")
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Server error...Please again later",
                            )
                        }catch (e:IOException){
                            _isLoading.value = false
                            Log.e("io error",e.localizedMessage?:"io error")
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Could not connect to the server... Please try again",
                            )
                        }
                    }else{
                        _isLoading.value = false
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "No internet connection was found",
                        )
                    }

                }
            }
        })
    }
}