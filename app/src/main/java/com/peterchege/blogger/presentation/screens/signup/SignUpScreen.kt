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
package com.peterchege.blogger.presentation.screens.signup

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    navigateToLoginScreen:() ->Unit,
    viewModel: SignUpScreenViewModel = hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SignUpScreenContent(
        navigateToLoginScreen = navigateToLoginScreen,
        uiState = uiState.value,
        eventFlow = viewModel.eventFlow,
        onChangeUsername = { viewModel.onChangeUsername(it) },
        onChangeEmail = { viewModel.onChangeEmail(it) },
        onChangeFullName = { viewModel.onChangeFullName(it) },
        onChangePassword = { viewModel.onChangePassword(it) },
        onChangeConfirmPassword = { viewModel.onChangePasswordConfirm(it) },
        onChangePasswordVisibility = { viewModel.onChangePasswordVisibility() },
        onSubmit = { viewModel.signUpUser(navigateToLoginScreen = navigateToLoginScreen) }
    )

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreenContent(
    navigateToLoginScreen: () -> Unit,
    uiState: SignUpFormState,
    eventFlow: SharedFlow<UiEvent>,
    onChangeUsername:(String) -> Unit,
    onChangeEmail:(String) -> Unit,
    onChangeFullName:(String) -> Unit,
    onChangePassword:(String) -> Unit,
    onChangeConfirmPassword:(String) -> Unit,
    onChangePasswordVisibility:() -> Unit,
    onSubmit:() -> Unit,

    ){

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true){
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier= Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier= Modifier
                .fillMaxSize()
                .padding(defaultPadding),
        ) {

            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Blogger App",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Sign Up",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.username ,
                    onValueChange ={
                        onChangeUsername(it)

                    },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.fullName ,
                    onValueChange ={
                        onChangeFullName(it)

                    },
                    label = { Text("Full Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =uiState.email ,
                    onValueChange ={
                        onChangeEmail(it)

                    },
                    label = { Text("Email Address") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.password ,
                    onValueChange ={
                        onChangePassword(it)
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (!uiState.isPasswordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            ,
                            contentDescription = "Visibility on",
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    onChangePasswordVisibility()
                                }
                        )

                    },
                    visualTransformation = if (!uiState.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
                    ,
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.confirmPassword ,
                    onValueChange ={
                        onChangeConfirmPassword(it)

                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (!uiState.isPasswordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff

                            ,
                            contentDescription = "Visibility on",
                            modifier = Modifier
                                .size(26.dp)
                                .clickable {
                                    onChangePasswordVisibility()
                                }
                        )

                    },
                    visualTransformation = if (!uiState.isPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation()
                    ,

                    label = { Text("Confirm Password") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                        keyboardController?.hide()
                        onSubmit()

                    }
                ) {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(30.dp))
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    onClick = {
                    navigateToLoginScreen()

                }) {
                    Text(text = "Login")
                }
            }
            if (uiState.isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }
    }
}