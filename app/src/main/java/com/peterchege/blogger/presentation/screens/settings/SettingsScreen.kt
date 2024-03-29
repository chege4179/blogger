/*
 * Copyright 2024 Blogger
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
package com.peterchege.blogger.presentation.screens.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.Constants
import com.peterchege.blogger.core.util.ThemeConfig
import com.peterchege.blogger.core.util.findActivity
import com.peterchege.blogger.core.util.showReviewDialog
import com.peterchege.blogger.core.util.toast
import com.peterchege.blogger.presentation.alertDialogs.SignOutDialog
import com.peterchege.blogger.presentation.alertDialogs.ThemeDialog
import com.peterchege.blogger.presentation.components.SettingsRow

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    navigateHome: () -> Unit,
    startOSSActivity: () -> Unit,
) {
    val context = LocalContext.current
    val activity = context.findActivity()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val authUser by viewModel.authUser.collectAsStateWithLifecycle()
    val fcmToken by viewModel.fcmToken.collectAsStateWithLifecycle()
    val theme by viewModel.theme.collectAsStateWithLifecycle()

    SettingsScreenContent(
        uiState = uiState,
        theme = theme,
        openSignOutDialog = viewModel::toggleSignOutDialog,
        openOSSMenu = startOSSActivity,
        openThemeDialog = viewModel::toggleThemeDialog,
        changeTheme = viewModel::changeTheme,
        reviewApp = {
            showReviewDialog(
                activity = activity,
                onComplete = {
                    context.toast(msg = "Review submitted successfully. Thank you")
                },
                onFailure = {
                    context.toast(msg = "Failed to add review")
                }
            )
        },
        signOutUser = {
            authUser?.let {
                if (it.userId != "") {
                    viewModel.signOutUser(
                        userId = it.userId,
                        fcmToken = fcmToken,
                        navigateToHome = navigateHome
                    )
                }
            }
        },

        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    theme: String,
    uiState: SettingScreenUiState,
    changeTheme: (String) -> Unit,
    openSignOutDialog: () -> Unit,
    openThemeDialog: () -> Unit,
    signOutUser: () -> Unit,
    openOSSMenu: () -> Unit,
    reviewApp: () -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings_header_name))
                }
            )

        }
    ) { paddingValues ->
        if (uiState.isThemeDialogOpen) {
            ThemeDialog(
                changeTheme = {
                    changeTheme(it)
                },
                toggleThemeDialog = openThemeDialog,
                currentTheme = theme,
            )
        }
        if (uiState.isSignOutDialogOpen) {
            SignOutDialog(
                signOut = signOutUser,
                closeSignOutDialog = openSignOutDialog
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                SettingsRow(
                    title = stringResource(id = R.string.theme),
                    onClick = openThemeDialog
                )
                Spacer(modifier = Modifier.height(10.dp))
                SettingsRow(
                    title = stringResource(id = R.string.license),
                    onClick = openOSSMenu

                )
                Spacer(modifier = Modifier.height(10.dp))
                SettingsRow(
                    title = stringResource(id = R.string.rate_us),
                    onClick = reviewApp

                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    shape = RoundedCornerShape(8.dp),
                    onClick = openSignOutDialog
                ) {
                    Text(text = stringResource(id = R.string.sign_out_button_text))
                }
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview1() {
    SettingsScreenContent(
        theme = ThemeConfig.DARK,
        uiState = SettingScreenUiState(),
        changeTheme = {},
        openSignOutDialog = { /*TODO*/ },
        openThemeDialog = { /*TODO*/ },
        signOutUser = { /*TODO*/ },
        openOSSMenu = {},
        reviewApp = {}
    )
}

@Preview
@Composable
fun SettingsScreenPreview2() {
    SettingsScreenContent(
        theme = ThemeConfig.DARK,
        uiState = SettingScreenUiState(isSignOutDialogOpen = true),
        changeTheme = {},
        openSignOutDialog = { /*TODO*/ },
        openThemeDialog = { /*TODO*/ },
        signOutUser = { /*TODO*/ },
        openOSSMenu = {},
        reviewApp = {}
    )
}

@Preview
@Composable
fun SettingsScreenPreview3() {
    SettingsScreenContent(
        theme = ThemeConfig.DARK,
        uiState = SettingScreenUiState(isThemeDialogOpen = true),
        changeTheme = {},
        openSignOutDialog = { /*TODO*/ },
        openThemeDialog = { /*TODO*/ },
        signOutUser = { /*TODO*/ },
        openOSSMenu = {},
        reviewApp = {}
    )
}