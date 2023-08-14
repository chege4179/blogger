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
package com.peterchege.blogger.presentation.screens.login

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.NetworkStatus
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navigateToSignUpScreen: () -> Unit,
    navigateToDashBoard:() -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()

) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val networkStatus = viewModel.networkStatus.collectAsStateWithLifecycle()


    LoginScreenContent(
        uiState = uiState.value,
        networkStatus = networkStatus.value,
        eventFlow = viewModel.eventFlow,
        onChangeUsername = { viewModel.onChangeUsername(it) },
        onChangePassword = { viewModel.onChangePassword(it) },
        onChangePasswordVisibility = { viewModel.onChangePasswordVisibility() },
        onSubmit = { viewModel.initiateLogin(navigateToDashBoard = navigateToDashBoard) } ,
        navigateToSignUpScreen = navigateToSignUpScreen,


        )


}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun LoginScreenContent(
    uiState: LoginFormState,
    networkStatus: NetworkStatus,
    navigateToSignUpScreen: () -> Unit,

    eventFlow: SharedFlow<UiEvent>,
    onChangeUsername: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,


    ) {
    val context = LocalContext.current

    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = networkStatus) {
        when (networkStatus) {
            is NetworkStatus.Unknown -> {

            }

            is NetworkStatus.Connected -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Connected"
                )
            }

            is NetworkStatus.Disconnected -> {
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "You are offline"
                )
            }
        }
    }

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
        ) {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.blogger_app_icon_foreground),
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp),
                    contentDescription = "App Icon"
                )
                Text(
                    text = "Blogger App",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Login ",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.username,
                    onValueChange = {
                        onChangeUsername(it)
                        //state.username = it
                    },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.password,
                    onValueChange = {
                        onChangePassword(it)
                    },
                    trailingIcon = {

                        Icon(
                            imageVector = if (!uiState.isPasswordVisible)
                                Icons.Filled.VisibilityOff
                            else
                                Icons.Filled.Visibility,
                            contentDescription = "Visibility on",
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    onChangePasswordVisibility()
                                }
                        )

                    },
                    visualTransformation = if (uiState.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        keyboardController?.hide()
                        onSubmit()

                    }
                )
                {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(20.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        navigateToSignUpScreen()

                    }) {
                    Text(text = "Sign Up")
                }
            }
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }

    }


}


