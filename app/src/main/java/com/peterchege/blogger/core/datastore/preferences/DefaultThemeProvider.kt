/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.peterchege.blogger.core.util.ThemeConfig
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class DefaultThemeProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val THEME = stringPreferencesKey(name = "theme")

    val theme = dataStore.data.map { preferences ->
        preferences[THEME] ?: ThemeConfig.FOLLOW_SYSTEM
    }

    suspend fun setTheme(themeValue:String){
        dataStore.edit { preferences ->
            preferences[THEME] = themeValue
        }
    }



}