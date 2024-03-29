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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.peterchege.blogger.presentation.components.AppLoader
import com.peterchege.blogger.presentation.components.CustomIconButton
import com.peterchege.blogger.presentation.components.OtpComponent
import com.peterchege.blogger.presentation.theme.defaultPadding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    navigateToLoginScreen: () -> Unit,
    viewModel: SignUpScreenViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    SignUpScreenContent(
        navigateToLoginScreen = navigateToLoginScreen,
        uiState = uiState.value,
        eventFlow = viewModel.eventFlow,
        onChangeUsername = viewModel::onChangeUsername,
        onChangeEmail = viewModel::onChangeEmail,
        onChangeFullName = viewModel::onChangeFullName,
        onChangePassword = viewModel::onChangePassword,
        onChangeConfirmPassword = viewModel::onChangePasswordConfirm,
        onChangePasswordVisibility = viewModel::onChangePasswordVisibility,
        onSubmit = viewModel::signUpUser,
        onChangeOTPInput = viewModel::onChangeOTPInput,
        onDismissOTP = viewModel::toggleOTPInputVisibility,
        onSubmitOTP = { viewModel.submitOTPInput(navigateToLoginScreen) }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreenContent(
    navigateToLoginScreen: () -> Unit,
    uiState: SignUpFormState,
    eventFlow: SharedFlow<UiEvent>,
    onChangeUsername: (String) -> Unit,
    onChangeEmail: (String) -> Unit,
    onChangeFullName: (String) -> Unit,
    onChangePassword: (String) -> Unit,
    onChangeConfirmPassword: (String) -> Unit,
    onChangePasswordVisibility: () -> Unit,
    onSubmit: () -> Unit,
    onSubmitOTP: () -> Unit,
    onChangeOTPInput: (String) -> Unit,
    onDismissOTP: () -> Unit,

    ) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }

    val annotatedString = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        ) {
            append("Already have an account ? ")
        }
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                textDecoration = TextDecoration.Underline
            )
        ) {
            pushStringAnnotation(tag = "login", annotation = "login")
            append("Login")
        }

    }

    LaunchedEffect(key1 = true) {
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
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        AppLoader(isLoading = uiState.isLoading)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(id = R.string.signup),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.username,
                onValueChange = {
                    onChangeUsername(it)

                },
                label = {
                    Text(text = stringResource(id = R.string.username))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.fullName,
                onValueChange = {
                    onChangeFullName(it)
                },
                label = {
                    Text(text = stringResource(id = R.string.fullName))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.email,
                onValueChange = {
                    onChangeEmail(it)

                },
                label = { Text(text = stringResource(id = R.string.email)) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.password,
                onValueChange = {
                    onChangePassword(it)
                },
                trailingIcon = {
                    CustomIconButton(
                        imageVector = if (!uiState.isPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        onClick = {
                            onChangePasswordVisibility()
                        },
                        contentDescription = if (!uiState.isPasswordVisible)
                            stringResource(id = R.string.visibility_on_description)
                        else
                            stringResource(id = R.string.visibility_off_description)
                    )
                },
                visualTransformation = if (!uiState.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                label = {
                    Text(text = stringResource(id = R.string.password))
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.confirmPassword,
                onValueChange = {
                    onChangeConfirmPassword(it)

                },
                trailingIcon = {
                    CustomIconButton(
                        imageVector = if (!uiState.isPasswordVisible)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff,
                        onClick = { onChangePasswordVisibility() },
                        contentDescription = if (!uiState.isPasswordVisible)
                            stringResource(id = R.string.visibility_on_description)
                        else
                            stringResource(id = R.string.visibility_off_description),

                        )
                },
                visualTransformation = if (!uiState.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),

                label = {
                    Text(text = stringResource(id = R.string.confirm_password))
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                onClick = {
                    keyboardController?.hide()
                    onSubmit()

                }
            ) {
                Text(text = stringResource(id = R.string.signup))
            }
            Spacer(modifier = Modifier.height(30.dp))
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(start = offset, end = offset)
                        .firstOrNull()
                        ?.let { span ->
                            when (span.tag) {
                                "login" -> {
                                    navigateToLoginScreen()
                                }
                            }
                        }
                }
            )
        }
    }
    if (uiState.isOtpBottomSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismissOTP
        ) {
            OtpComponent(
                onSubmitClicked = onSubmitOTP,
                onCancelClicked = onDismissOTP,
                otpInputState = uiState.otpInput,
                otpEmailSentTo = uiState.email,
                onOtpInputStateChange = onChangeOTPInput
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun SignUpScreenPreview() {
    SignUpScreenContent(
        navigateToLoginScreen = { /*TODO*/ },
        uiState = SignUpFormState(isLoading = true),
        eventFlow = MutableSharedFlow(),
        onChangeUsername = {},
        onChangeEmail = {},
        onChangeFullName = {},
        onChangePassword = {},
        onChangeConfirmPassword = {},
        onChangePasswordVisibility = { /*TODO*/ },
        onSubmit = { /*TODO*/ },
        onSubmitOTP = { /*TODO*/ },
        onChangeOTPInput = {},
        onDismissOTP = {}
    )

}