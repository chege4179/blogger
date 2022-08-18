package com.peterchege.blogger.ui.dashboard.profile_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.api.requests.FollowUser
import com.peterchege.blogger.api.responses.Follower
import com.peterchege.blogger.api.responses.Following
import com.peterchege.blogger.ui.post_screen.PostRepository
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ProfileFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,
    private val sharedPreferences: SharedPreferences,
    private val repository: PostRepository,

):ViewModel() {
    private var _type = mutableStateOf<String>("")
    var type: State<String> = _type

    private var _isLoading = mutableStateOf<Boolean>(false)
    var isLoading: State<Boolean> = _isLoading
    init {
        getType()
        getProfile()
    }

    private fun getType(){
        savedStateHandle.get<String>("type")?.let{
            _type.value = it

        }
    }


    private val _userFollowers = mutableStateOf<List<Follower>>(emptyList())
    val userFollowers :State<List<Follower>> = _userFollowers

    private val _userFollowing = mutableStateOf<List<Following>>(emptyList())
    val userFollowing :State<List<Following>> = _userFollowing

    fun getIsFollowingStatus(username:String):Boolean{
        val followingUsernames = _userFollowing.value.map { it.followedUsername }
        return followingUsernames.contains(username)
    }
    fun followUser(followedUsername: String){
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
                        followedUsername = followedUsername,
                    )
                )

            }

        }catch (e: HttpException){
            Log.e("http error",e.localizedMessage?:"A http error occurred")
        }catch (e: IOException){
            Log.e("io error",e.localizedMessage?:"An io error occurred")
        }



    }
    fun unfollowUser(followedUsername: String){
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
                        followedUsername = followedUsername,
                    )
                )
            }
        }catch (e: HttpException){
            Log.e("http error",e.localizedMessage?:"A http error occurred")
        }catch (e: IOException){
            Log.e("io error",e.localizedMessage?:"An io error occurred")
        }

    }



    private fun getProfile(){
        _isLoading.value = true
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        profileUseCase(username = username!!).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _isLoading.value = false
                    _userFollowers.value = result.data?.user?.followers ?: emptyList()
                    _userFollowing.value = result.data?.user?.following ?: emptyList()
                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
                is Resource.Loading -> {
                    _isLoading.value = false
                }
            }

        }.launchIn(viewModelScope)

    }
}