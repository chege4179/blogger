package com.peterchege.blogger.ui.author_profile

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.api.requests.FollowUser
import com.peterchege.blogger.api.requests.LogoutUser
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.api.responses.User
import com.peterchege.blogger.ui.dashboard.profile_screen.GetProfileUseCase
import com.peterchege.blogger.ui.login.LogoutUseCase
import com.peterchege.blogger.ui.post_screen.PostRepository
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import com.peterchege.blogger.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class AuthorProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val sharedPreferences: SharedPreferences,
    private val repository: PostRepository,

):ViewModel() {

    private var _state = mutableStateOf<ProfileResponseState>(ProfileResponseState())
    var state : State<ProfileResponseState> = _state

    private var _isLoading = mutableStateOf(false)
    var isLoading : State<Boolean> = _isLoading

    private var _isFound = mutableStateOf(false)
    var isFound :State<Boolean> = _isFound

    private var _isFollowing = mutableStateOf(false)
    var isFollowing :State<Boolean> = _isFollowing

    data class ProfileResponseState(
        val msg:String = "",
        val success:Boolean = false,
        val user: User? = null,
        val posts: List<Post> = emptyList()
    )
    init {
        val username = savedStateHandle.get<String>("username")
        Log.e("username",username!!)
        savedStateHandle.get<String>("username")?.let { username ->
            Log.e("author",username)
            getProfile(username = username)
        }

    }
    fun getFollowingStatus(){
        val followers = _state.value.user?.followers
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        val followerUsernames = followers?.map { it.followerUsername }
        _isFollowing.value = followerUsernames!!.contains(username)

    }


    fun followUser(){
        try {
            val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
            val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME,null)
            val userId = sharedPreferences.getString(Constants.LOGIN_ID,null)
            viewModelScope.launch {
                val followResponse = repository.followUser(
                    FollowUser(
                    followerUsername = username!!,
                    followerFullname = fullname!!,
                    followerId = userId!!,
                    followedUsername = _state.value.user!!.username,
                    )
                )
                if (followResponse.success){
                    _isFollowing.value = true
                }
            }

        }catch (e: HttpException){
            Log.e("http error",e.localizedMessage?:"A http error occurred")
        }catch (e: IOException){
            Log.e("io error",e.localizedMessage?:"An io error occurred")
        }
    }
    fun unfollowUser(){
        try {
            val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
            val fullname = sharedPreferences.getString(Constants.LOGIN_FULLNAME,null)
            val userId = sharedPreferences.getString(Constants.LOGIN_ID,null)
            viewModelScope.launch {
                val followResponse = repository.unfollowUser(
                    FollowUser(
                    followerUsername = username!!,
                    followerFullname = fullname!!,
                    followerId = userId!!,
                    followedUsername = _state.value.user!!.username,
                )
                )
                if (followResponse.success){
                    _isFollowing.value = false
                }
            }

        }catch (e: HttpException){
            Log.e("http error",e.localizedMessage?:"A http error occurred")
        }catch (e: IOException){
            Log.e("io error",e.localizedMessage?:"An io error occurred")
        }

    }


    private fun getProfile(username:String){
        profileUseCase(username = username).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _isLoading.value = false
                    _isFound.value = true
                    result.data?.user?.let { Log.e("user", it.username) }
                    _state.value = ProfileResponseState(
                        msg= result.data!!.msg,
                        success = result.data.success,
                        posts = result.data.posts,
                        user = result.data.user,
                    )
                    getFollowingStatus()


                }
                is Resource.Error -> {
                    _isLoading.value = false
                    _isFound.value = false
                }
                is Resource.Loading -> {
                    _isLoading.value = true

                }
            }

        }.launchIn(viewModelScope)

    }

}