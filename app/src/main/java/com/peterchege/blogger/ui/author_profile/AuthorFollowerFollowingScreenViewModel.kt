package com.peterchege.blogger.ui.author_profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.api.responses.Follower
import com.peterchege.blogger.api.responses.Following
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.api.responses.User
import com.peterchege.blogger.ui.dashboard.profile_screen.GetProfileUseCase
import com.peterchege.blogger.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class AuthorFollowerFollowingScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileUseCase: GetProfileUseCase,

    ):ViewModel() {
    private var _type = mutableStateOf<String>("")
    var type: State<String> = _type

    private var _user = mutableStateOf<User?>(null)
    var user: State<User?> = _user

    private var _isLoading = mutableStateOf(false)
    var isLoading : State<Boolean> = _isLoading

    private var _isFound = mutableStateOf(false)
    var isFound :State<Boolean> = _isFound

    private var _success = mutableStateOf(false)
    var success :State<Boolean> = _success

    private var _msg = mutableStateOf("")
    var msg :State<String> = _msg

    private var _posts = mutableStateOf<List<Post>>(emptyList())
    var posts :State<List<Post>> = _posts

    private val _authorFollowers = mutableStateOf<List<Follower>>(emptyList())
    val authorFollowers :State<List<Follower>> = _authorFollowers

    private val _authorFollowing = mutableStateOf<List<Following>>(emptyList())
    val authorFollowing :State<List<Following>> = _authorFollowing


    init {
        savedStateHandle.get<String>("username")?.let{ username->
            Log.e("Author Username",username)
            getProfile(username = username)
        }
        getType()
    }
    private fun getType(){
        savedStateHandle.get<String>("type")?.let{
            _type.value = it

        }
    }
    fun getIsFollowingStatus(username:String):Boolean{
        val followingUsernames = _user.value?.following?.map { it.followedUsername }
        if (followingUsernames != null) {
            return followingUsernames.contains(username)
        }else{
            return false
        }
    }
    private fun getProfile(username:String){
        profileUseCase(username = username).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _user.value = result.data?.user
                    _authorFollowers.value = result.data?.user?.followers ?: emptyList()
                    _authorFollowing.value = result.data?.user?.following ?: emptyList()
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }

        }.launchIn(viewModelScope)

    }
}