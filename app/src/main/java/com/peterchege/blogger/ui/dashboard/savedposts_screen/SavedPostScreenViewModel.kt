package com.peterchege.blogger.ui.dashboard.savedposts_screen

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.peterchege.blogger.ui.post_screen.PostRepository
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SavedPostScreenViewModel @Inject constructor (
    private val repository: PostRepository,
    private val sharedPreferences: SharedPreferences

):ViewModel() {
    val posts = repository.getAllPostsFromRoom()

    fun onProfileNavigate(username:String, bottomNavController: NavController, navHostController: NavHostController){
        val loginUsername = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        Log.e("Post author",username)
        if (loginUsername != null) {
            Log.e("Login Username",loginUsername)
        }
        if (loginUsername == username){
            bottomNavController.navigate(Screens.PROFILE_SCREEN)
        }else{
            navHostController.navigate(Screens.AUTHOR_PROFILE_NAVIGATION)
        }

    }



}