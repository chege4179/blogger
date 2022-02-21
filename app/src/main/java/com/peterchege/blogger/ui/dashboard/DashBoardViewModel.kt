package com.peterchege.blogger.ui.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DashBoardViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
):ViewModel() {

    var username by mutableStateOf(savedStateHandle.get("username") ?: "Empty string")
    var id by mutableStateOf(savedStateHandle.get("id") ?: "Empty id")


}