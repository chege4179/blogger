package com.peterchege.blogger.data

import com.google.firebase.messaging.FirebaseMessaging
import com.peterchege.blogger.domain.FcmTokenRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FcmTokenRepositoryImpl @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging
) :FcmTokenRepository{


    override suspend fun getFcmToken(): String {
        return firebaseMessaging.token.await()
    }
}