package com.peterchege.blogger.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val AUTH_TOKEN = stringPreferencesKey(name = "auth_token")
class DefaultAuthTokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    val authToken : Flow<String> = dataStore.data.map { preferences ->
        preferences[AUTH_TOKEN] ?: ""
    }

    suspend fun setAuthToken(token: String) {
        dataStore.edit { preferences ->
            preferences[AUTH_TOKEN] = token
        }
    }

}