package com.peterchege.blogger.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peterchege.blogger.core.datastore.preferences.DefaultThemeProvider
import com.peterchege.blogger.core.util.ThemeConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val defaultThemeProvider: DefaultThemeProvider,
) : ViewModel(){

    val theme = defaultThemeProvider.theme
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ThemeConfig.FOLLOW_SYSTEM
        )
}