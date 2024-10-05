package com.peterchege.blogger.domain

interface FcmTokenRepository {


    suspend fun getFcmToken():String
}