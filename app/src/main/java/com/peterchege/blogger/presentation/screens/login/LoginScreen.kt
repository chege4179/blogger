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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peterchege.blogger.R
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.repository.NetworkStatus
import com.peterchege.blogger.presentation.components.AppBackgroundImage
import com.peterchege.blogger.presentation.components.AppLoader
import com.peterchege.blogger.presentation.theme.BloggerTheme
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(
    navigateToSignUpScreen: () -> Unit,
    navigateToDashBoard: () -> Unit,
    viewModel: LoginScreenViewModel = hiltViewModel()

) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val networkStatus = viewModel.networkStatus.collectAsStateWithLifecycle()


    LoginScreenContent(
        uiState = uiState.value,
        networkStatus = networkStatus.value,
        eventFlow = viewModel.eventFlow,
        onChangeEmail = { viewModel.onChangeEmail(it) },
        onChangePassword = { viewModel.onChangePassword(it) },
        onChangePasswordVisibility = { viewModel.onChangePasswordVisibility() },
        onSubmit = { viewModel.initiateLogin(navigateToDashBoard = navigateToDashBoard) },
        navigateToSignUpScreen = navigateToSignUpScreen,
    )


}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun LoginScreenContent(
    uiState: LoginFormState,
    networkStatus: NetworkStatus,
    navigateToSignUpScreen: () -> Unit,
    eventFlow: SharedFlow<UiEvent>,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,


    ) {
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current

    val offlineMessage = stringResource(id = R.string.offline_message)
    LaunchedEffect(key1 = networkStatus) {

        when (networkStatus) {
            is NetworkStatus.Unknown -> {}
            is NetworkStatus.Connected -> {}
            is NetworkStatus.Disconnected -> {
                snackbarHostState.showSnackbar(
                    message = offlineMessage
                )
            }
        }
    }

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            append("Don't have an account yet ? ")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.onBackground
            )
        ) {
            pushStringAnnotation(tag = "signup", annotation = "signup")
            append("Sign Up")
        }

    }

    LaunchedEffect(key1 = Unit) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {}
            }
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        AppLoader(isLoading = uiState.isLoading)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(defaultPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.mipmap.app_icon_foreground),
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                ,
                contentDescription = stringResource(id = R.string.app_name)
            )
            Text(
                text = stringResource(id = R.string.login_header),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = R.string.login),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(15.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = {
                    onChangeEmail(it)
                    //state.username = it
                },
                label = {
                    Text(text = stringResource(id = R.string.email))
                }
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
                        contentDescription = if (!uiState.isPasswordVisible)
                            stringResource(id = R.string.visibility_on_description)
                        else
                            stringResource(id = R.string.visibility_off_description),
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
                label = {
                    Text(text = stringResource(id = R.string.password))
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    keyboardController?.hide()
                    onSubmit()
                }
            )
            {
                Text(text = stringResource(id = R.string.login))
            }
            Spacer(modifier = Modifier.height(20.dp))
            ClickableText(
                text = annotatedString,

                onClick = { offset ->
                    annotatedString.getStringAnnotations(start = offset, end = offset)
                        .firstOrNull()
                        ?.let { span ->
                            when (span.tag) {
                                "signup" -> {
                                    navigateToSignUpScreen()
                                }
                            }
                        }
                }
            )
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun LoginScreenPreview1() {
    BloggerTheme(darkTheme = true){
        LoginScreenContent(
            uiState = LoginFormState(isLoading = true),
            networkStatus = NetworkStatus.Connected,
            navigateToSignUpScreen = { /*TODO*/ },
            eventFlow = MutableSharedFlow<UiEvent>(),
            onChangeEmail = {},
            onChangePassword = {},
            onChangePasswordVisibility = { /*TODO*/ },
            onSubmit = {}
        )
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun LoginScreenPreview2() {
    BloggerTheme(darkTheme = false){
        LoginScreenContent(
            uiState = LoginFormState(isLoading = true),
            networkStatus = NetworkStatus.Connected,
            navigateToSignUpScreen = { /*TODO*/ },
            eventFlow = MutableSharedFlow<UiEvent>(),
            onChangeEmail = {},
            onChangePassword = {},
            onChangePasswordVisibility = { /*TODO*/ },
            onSubmit = {}
        )
    }

}
