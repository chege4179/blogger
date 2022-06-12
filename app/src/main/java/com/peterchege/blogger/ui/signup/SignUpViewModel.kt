package com.peterchege.blogger.ui.signup

import android.content.Context
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.api.requests.SignUpUser
import com.peterchege.blogger.util.Resource
import com.peterchege.blogger.util.Screens

import com.peterchege.blogger.util.TextFieldState
import com.peterchege.blogger.util.hasInternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val signUpRepository: SignUpRepository,

):ViewModel() {

    private var _isLoading = mutableStateOf(false)
    var isLoading :State<Boolean> = _isLoading

    private var _usernameState = mutableStateOf(TextFieldState())
    var usernameState: State<TextFieldState> = _usernameState

    private var _fullnameState = mutableStateOf(TextFieldState())
    var fullnameState: State<TextFieldState> = _fullnameState

    private var _emailState = mutableStateOf(TextFieldState())
    var emailState: State<TextFieldState> = _emailState

    private var _passwordState = mutableStateOf(TextFieldState())
    var passwordState: State<TextFieldState> =  _passwordState


    private var _confirmPasswordState = mutableStateOf(TextFieldState())
    var confirmPasswordState: State<TextFieldState> =  _confirmPasswordState

    private val _passwordVisibility = mutableStateOf(false)
    var passwordVisibility: State<Boolean> =  _passwordVisibility

    fun onChangePasswordVisibility(){
        _passwordVisibility.value = !_passwordVisibility.value
    }


    fun OnChangeUsername(text:String){
        _usernameState.value = TextFieldState(text = text)
    }
    fun OnChangePassword(text:String){
        _passwordState.value = TextFieldState(text = text)

    }
    fun OnChangeConfirmPassword(text:String){
        _confirmPasswordState.value = TextFieldState(text = text)

    }
    fun OnChangeEmail(text:String){
        _emailState.value = TextFieldState(text = text)

    }
    fun OnChangeFullName(text:String){
        _fullnameState.value = TextFieldState(text = text)

    }
    fun signUpUser(navController: NavController,scaffoldState: ScaffoldState,context: Context){
        viewModelScope.launch {
            _isLoading.value = true

            if (_confirmPasswordState.value.text.trim() != _passwordState.value.text.trim()){
                _isLoading.value = false
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Passwords do not match",
                )
            }else{
                if (hasInternetConnection(context)){
                    val signUpUser = SignUpUser(_usernameState.value.text.trim(),_fullnameState.value.text.trim(),_passwordState.value.text.trim(),_emailState.value.text.trim())
                    try {
                        val signUpResponse = signUpRepository.signUpUser(signUpUser = signUpUser)
                        _isLoading.value = false
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = signUpResponse.msg
                        )
                        if (signUpResponse.success){
                            navController.navigate(Screens.LOGIN_SCREEN)
                        }
                    }catch (e:HttpException){
                        _isLoading.value = false
                        Log.e("http error",e.localizedMessage ?: "Server error...Please try again later")
                        scaffoldState.snackbarHostState.showSnackbar(
                            message =   "Server error...Please try again later"
                        )
                    }catch (e:IOException){
                        _isLoading.value = false
                        scaffoldState.snackbarHostState.showSnackbar(
                            message =  "Could not connect to the server...Please try again later"
                        )
                    }
                }else{
                    _isLoading.value = false
                    scaffoldState.snackbarHostState.showSnackbar(
                        message =  "No internet connection was found"
                    )
                }

            }
        }
    }
}