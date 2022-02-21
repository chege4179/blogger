package com.peterchege.blogger.ui.dashboard.home_screen.feed_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.api.responses.User
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import com.peterchege.blogger.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val sharedPreferences: SharedPreferences,
    private val api:BloggerApi
): ViewModel() {

    data class FeedListState(
        val msg:String = "",
        val posts:List<Post> = emptyList(),
        val users:List<User> = emptyList(),
        val success:Boolean = false,
        val error:String = "",
        val isLoading:Boolean = false,

    )
    private val _state = mutableStateOf(FeedListState())
    val state : State<FeedListState> = _state

    private val _searchTerm = mutableStateOf("")
    val searchTerm : State<String> = _searchTerm

    private val _isFound = mutableStateOf(true)
    val isFound :State<Boolean> = _isFound

    private var searchJob :Job? = null


    fun onChangeSearchTerm(searchTerm: String){
        _searchTerm.value = searchTerm
        if (searchTerm.length > 3){
            _state.value = FeedListState(isLoading = true)
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                try {
                    val response = api.searchPost(searchTerm = searchTerm)
                    _state.value = FeedListState(
                        msg = response.msg,
                        posts = response.posts,
                        success =response.success,
                        users = response.users,
                        error ="",
                        isLoading = false
                    )
                    if (response.posts.isEmpty() && response.users.isEmpty()){
                        _isFound.value = false
                    }
                }catch (e:HttpException){
                    Log.e("http error",e.localizedMessage?: "a http error occurred")
                    _state.value = FeedListState(
                        msg="A http error ocurred",
                        isLoading = false
                    )
                }catch (e:IOException){
                    Log.e("io error",e.localizedMessage?: "a http error occurred")
                    _state.value = FeedListState(
                        msg="A io error occurred",
                        isLoading = false
                    )
                }

            }
        }else if (searchTerm.length < 2){
            viewModelScope.launch {
                delay(500L)
                _isFound.value = true
                getFeedPosts()
            }

        }
    }
    init {
        getFeedPosts()
    }
    fun onProfileNavigate(username:String,bottomNavController: NavController,navHostController: NavHostController){
        val loginUsername = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        Log.e("Post author",username)
        if (loginUsername != null) {
            Log.e("Login Username",loginUsername)
        }
        if (loginUsername == username){
            bottomNavController.navigate(Screens.PROFILE_NAVIGATION)
        }else{
            navHostController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/$username")
        }

    }

    private fun getFeedPosts(){
        getFeedUseCase().onEach { result ->
            when(result){
                is Resource.Success -> {
                    _state.value = FeedListState(posts = result.data ?: emptyList(), isLoading = false)
                }
                is Resource.Error -> {
                    _state.value = FeedListState(error = result.message ?: "An error occurred", isLoading = false)
                }
                is Resource.Loading -> {
                    _state.value = FeedListState(isLoading = true )

                }
            }
        }.launchIn(viewModelScope)
    }
}