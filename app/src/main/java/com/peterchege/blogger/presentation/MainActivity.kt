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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.metrics.performance.JankStats
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.peterchege.blogger.R
import com.peterchege.blogger.core.services.UploadPostService
import com.peterchege.blogger.core.util.ThemeConfig
import com.peterchege.blogger.presentation.components.AppBackgroundImage
import com.peterchege.blogger.presentation.navigation.AppNavigation
import com.peterchege.blogger.presentation.theme.BloggerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
@ExperimentalCoilApi
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

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
        setContent {
            val viewModel: MainActivityViewModel = hiltViewModel()
            val theme by viewModel.theme.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            BloggerTheme(
                darkTheme = shouldUseDarkTheme(theme = theme)
            ) {
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

    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
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