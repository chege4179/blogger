package com.peterchege.blogger.core.di

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.peterchege.blogger.core.firebase.config.RemoteConfigConfig
import com.peterchege.blogger.core.firebase.config.RemoteFeatureToggle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Provides
    @Singleton
    fun provideRemoteFeatureToggle(): RemoteFeatureToggle =
        RemoteFeatureToggle(remoteConfig = Firebase.remoteConfig)

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = RemoteConfigConfig.setup()




}