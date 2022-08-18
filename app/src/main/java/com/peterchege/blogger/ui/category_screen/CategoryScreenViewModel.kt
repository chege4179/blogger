package com.peterchege.blogger.ui.category_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CategoryScreenViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,

) :ViewModel() {
    private var _category = mutableStateOf("")
    var category : State<String> = _category
    init{
        savedStateHandle.get<String>("category")?.let { category ->
            _category.value = category
        }
    }
}