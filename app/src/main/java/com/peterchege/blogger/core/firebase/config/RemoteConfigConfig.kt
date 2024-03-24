
package com.peterchege.blogger.core.firebase.config

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.peterchege.blogger.BuildConfig
import com.peterchege.blogger.R
import java.util.concurrent.TimeUnit

object RemoteConfigConfig {

    private val minimumFetchInterval = if (BuildConfig.DEBUG) {
        TimeUnit.MINUTES.toSeconds(5L)
    } else {
        TimeUnit.MINUTES.toSeconds(12L)
    }

    fun setup() = Firebase.remoteConfig.apply {
        setDefaultsAsync(R.xml.remote_config_defaults)
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = minimumFetchInterval
        }
        setConfigSettingsAsync(configSettings)
    }
}