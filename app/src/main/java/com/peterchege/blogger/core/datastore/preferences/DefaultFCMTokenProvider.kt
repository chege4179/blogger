package com.peterchege.blogger.core.datastore.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultFCMTokenProvider @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val FCM_TOKEN = stringPreferencesKey(name = "fcm_token")


    val fcmToken : Flow<String> = dataStore.data.map { preferences ->
        preferences[FCM_TOKEN] ?: ""
    }

    suspend fun setFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[FCM_TOKEN] = token
        }
    }
}