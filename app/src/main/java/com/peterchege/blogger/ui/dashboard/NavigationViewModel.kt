package com.peterchege.blogger.ui.dashboard

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Screens
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences
):ViewModel() {
    fun getInitialRoute():String {
        val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
        if (username === null){
            return Screens.LOGIN_SCREEN
        }else{
            return Screens.DASHBOARD_SCREEN
        }
    }
}