package com.peterchege.blogger.ui.dashboard.profile_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.api.requests.CommentBody
import com.peterchege.blogger.ui.login.LogoutUseCase
import com.peterchege.blogger.api.requests.LogoutUser
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.api.responses.ProfileResponse
import com.peterchege.blogger.api.responses.User
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import com.peterchege.blogger.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val logoutUseCase: LogoutUseCase,
    private val profileUseCase: GetProfileUseCase,
):ViewModel() {
    val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)

    private var _state = mutableStateOf<ProfileResponseState>(ProfileResponseState())
    var state : State<ProfileResponseState> = _state

    private var _isLoading = mutableStateOf(false)
    var isLoading :State<Boolean> = _isLoading

    private var _openDialogState =  mutableStateOf(false)
    var openDialogState :State<Boolean> = _openDialogState

    data class ProfileResponseState(
        val msg:String = "",
        val success:Boolean = false,
        val user: User? = null,
        val posts: List<Post> = emptyList()
    )
    init {
        getProfile()
    }
    fun onDialogConfirm(scaffoldState: ScaffoldState,postTitle:String,postId:String) {
        _openDialogState.value = false
        viewModelScope.launch {

        }
    }
    fun onDialogOpen(postTitle:String) {
        _openDialogState.value = true
    }
    fun onDialogDismiss() {
        _openDialogState.value = false
    }
    fun logoutUser(navController: NavController){
        val sharedPref:SharedPreferences? = null

        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        val id = sharedPreferences.getString(Constants.LOGIN_ID,null)
        val token = sharedPreferences.getString(Constants.FCM_TOKEN,null)
        Log.d("username",username!!)
        if (token != null) {
            Log.d("deviceId",token)
        }

        logoutUseCase(LogoutUser(username = username, token = token ?:"",id=  id!!)).onEach { result ->
            when(result){
                is Resource.Success -> {
                    Log.d("Logout","success")
                    navController.navigate(Screens.LOGIN_SCREEN)
                    sharedPreferences.edit().remove(Constants.LOGIN_ID).commit()
                    sharedPreferences.edit().remove(Constants.LOGIN_USERNAME).commit()
                    sharedPreferences.edit().remove(Constants.LOGIN_FULLNAME).commit()
                    sharedPreferences.edit().remove(Constants.LOGIN_IMAGEURL).commit()
                    sharedPreferences.edit().remove(Constants.LOGIN_PASSWORD).commit()
                    sharedPreferences.edit().remove(Constants.LOGIN_EMAIL).commit()
                }
                is Resource.Loading -> {
                    Log.d("Logout","loading")
                }
                is Resource.Error -> {
                    Log.d("Logout","error")
                }
            }

        }.launchIn(viewModelScope)



    }



    private fun getProfile(){
        profileUseCase(username = username!!).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _isLoading.value = false
                    _state.value = ProfileResponseState(
                        msg= result.data!!.msg,
                        success = result.data.success,
                        posts = result.data.posts,
                        user = result.data.user,
                    )

                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
                is Resource.Loading -> {
                    _isLoading.value = true

                }
            }

        }.launchIn(viewModelScope)

    }
}