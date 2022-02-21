package com.peterchege.blogger.ui.dashboard.notifcations_screen

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.api.requests.Notification
import com.peterchege.blogger.ui.dashboard.profile_screen.GetProfileUseCase
import com.peterchege.blogger.util.Constants
import com.peterchege.blogger.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    val username = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)
    private var _notifications = mutableStateOf<List<Notification>>(emptyList())
    var notifications : State<List<Notification>> = _notifications

    private var _isLoading = mutableStateOf(false)
    var isLoading:State<Boolean> = _isLoading
    init {
        getNotifications()
    }


    private fun getNotifications(){
        getProfileUseCase(username!!).onEach { result ->
            when(result){
                is Resource.Success -> {
                    _isLoading.value = false
                    _notifications.value = result.data!!.user.notifications
                }
                is Resource.Loading -> {
                    _isLoading.value = true
                }
                is Resource.Error -> {
                    _isLoading.value = false
                }
            }

        }.launchIn(viewModelScope)

    }
}