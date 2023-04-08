package com.peterchege.blogger.core.datastore.repository

import android.content.Context
import androidx.datastore.dataStore
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.datastore.serializers.UserInfoSerializer
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.stateIn

val Context.userDataStore by dataStore("user.json", UserInfoSerializer)

class UserDataStoreRepository(
    @ApplicationContext private val context: Context
) {

    fun getLoggedInUser(): Flow<User?> {
        return context.userDataStore.data
    }
    suspend fun setLoggedInUser(user: User) {
        context.userDataStore.updateData {
            user
        }
    }
    suspend fun unsetLoggedInUser() {
        context.userDataStore.updateData {
            null
        }
    }
}