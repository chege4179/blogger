package com.peterchege.blogger.core.firebase.config

import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import timber.log.Timber

class RemoteFeatureToggle(
    private val remoteConfig: FirebaseRemoteConfig
) {
    val TAG = RemoteFeatureToggle::class.java.simpleName

    fun sync() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.tag(TAG).w("Successfully fetched remote config from Firebase")
                } else {
                    Timber.tag(TAG).w("Failed to fetch remote config from Firebase")
                }
            }
            .addOnFailureListener{
                Timber.tag(TAG).w("Failed to fetch remote config from Firebase failure")
                Timber.tag(TAG).w("Failed to fetch remote config from Firebase ${it.message}")
                Timber.tag(TAG).w("Failed to fetch remote config from Firebase ${it.localizedMessage}")
                it.printStackTrace()

            }
    }

    fun checkForUpdates() {
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                Timber.tag(TAG).w("Updated keys: " + configUpdate.updatedKeys);
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Timber.tag(TAG).w(error, "Config update error with code: %s", error.code)
            }
        })
    }

    fun getString(key: String): String = remoteConfig.getString(key)
}