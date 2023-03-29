/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.presentation.screens.dashboard

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.Screens
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