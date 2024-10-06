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
package com.peterchege.blogger.presentation

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.metrics.performance.JankStats
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.installStatus
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.requests.CaptureDeviceInfoDto
import com.peterchege.blogger.core.datastore.repository.UserDataStoreRepository
import com.peterchege.blogger.core.device.DeviceInfo
import com.peterchege.blogger.core.services.UploadPostService
import com.peterchege.blogger.core.util.ThemeConfig
import com.peterchege.blogger.core.util.isNotNull
import com.peterchege.blogger.domain.FcmTokenRepository
import com.peterchege.blogger.domain.repository.DeviceInfoRepository
import com.peterchege.blogger.presentation.components.AppBackgroundImage
import com.peterchege.blogger.presentation.navigation.AppNavigation
import com.peterchege.blogger.presentation.theme.BloggerTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {

    val tag = MainActivity::class.java.simpleName

    @Inject
    lateinit var deviceInfoRepository: DeviceInfoRepository

    @Inject
    lateinit var userDataStoreRepository: UserDataStoreRepository

    @Inject
    lateinit var fcmTokenRepository: FcmTokenRepository

    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateAvailable = MutableStateFlow(false)
    private var updateInfo: AppUpdateInfo? = null
    private val updateListener = InstallStateUpdatedListener { state: InstallState ->
        Timber.tag(tag).i("Install status ${state.installStatus}")
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            appUpdateManager.completeUpdate()
        } else if (state.installStatus() == InstallStatus.INSTALLED) {
            removeInstallStateUpdateListener()
        } else if (state.installStatus() == InstallStatus.FAILED) {
            removeInstallStateUpdateListener()
        } else if (state.installStatus() == InstallStatus.UNKNOWN) {
            removeInstallStateUpdateListener()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }
        try {
            appUpdateManager = AppUpdateManagerFactory.create(this)
            appUpdateManager.registerListener(updateListener)
            checkForUpdate()
        } catch (e: Exception) {
            e.printStackTrace()
            Timber.tag(tag).e("Try check update info exception: ${e.message}")
        }
        setContent {

            val viewModel: MainActivityViewModel = hiltViewModel()
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            BloggerTheme(darkTheme = shouldUseDarkTheme(theme = theme)) {
                AppBackgroundImage()
                AppNavigation(
                    navController = navController,
                    startOSSActivity = {
                        startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                    }
                )
            }

        }

    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true

        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    appUpdateManager.completeUpdate()
                } else if (appUpdateInfo.installStatus() == InstallStatus.INSTALLED) {
                    removeInstallStateUpdateListener()
                } else if (appUpdateInfo.installStatus() == InstallStatus.FAILED) {
                    removeInstallStateUpdateListener()
                } else if (appUpdateInfo.installStatus() == InstallStatus.UNKNOWN) {
                    removeInstallStateUpdateListener()
                }
            }

    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }

    private fun removeInstallStateUpdateListener() {
        appUpdateManager.unregisterListener(updateListener)
    }

    private fun startForInAppUpdate(it: AppUpdateInfo?) {
        appUpdateManager.startUpdateFlowForResult(it!!, AppUpdateType.FLEXIBLE, this, 1101)
    }

    private fun checkForUpdate() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            Timber.tag(tag).e("Update info: ${it.availableVersionCode()}")
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                updateInfo = it
                updateAvailable.update { true }
                startForInAppUpdate(updateInfo)
            } else {
                updateAvailable.update { false }
            }
        }
    }
}


@Composable
private fun shouldUseDarkTheme(
    theme: String,
): Boolean = when (theme) {
    ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    ThemeConfig.LIGHT -> false
    ThemeConfig.DARK -> true
    else -> isSystemInDarkTheme()
}