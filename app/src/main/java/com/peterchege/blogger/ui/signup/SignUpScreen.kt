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
package com.peterchege.blogger.ui.signup

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.util.Screens

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: SignUpViewModel = hiltViewModel()
){
    val context  = LocalContext.current
    val isLoading =  viewModel.isLoading.value
    val usernameState = viewModel.usernameState.value
    val fullnameState = viewModel.fullnameState.value
    val emailState = viewModel.emailState.value
    val passwordState = viewModel.passwordState.value
    val confirmPasswordState = viewModel.confirmPasswordState.value
    val scaffoldState = rememberScaffoldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Scaffold(
        scaffoldState = scaffoldState,
        modifier= Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier= Modifier
                .fillMaxSize()
                .padding(20.dp),
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
                    value = usernameState.text ,
                    onValueChange ={
                        viewModel.OnChangeUsername(it)

                    },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = fullnameState.text ,
                    onValueChange ={
                        viewModel.OnChangeFullName(it)

                    },
                    label = { Text("Full Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailState.text ,
                    onValueChange ={
                        viewModel.OnChangeEmail(it)

                    },
                    label = { Text("Email Address") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordState.text ,
                    onValueChange ={

                        viewModel.OnChangePassword(it)

                    },
                    trailingIcon = {
                        if(viewModel.passwordVisibility.value){
                            Icon(
                                Icons.Filled.Visibility,
                                contentDescription = "Visibility on",
                                Modifier
                                    .size(26.dp)
                                    .clickable {
                                        viewModel.onChangePasswordVisibility()
                                    }
                            )
                        }else{
                            Icon(
                                Icons.Filled.VisibilityOff,
                                contentDescription = "Visibility Off",
                                Modifier
                                    .size(26.dp)
                                    .clickable {
                                        viewModel.onChangePasswordVisibility()
                                    }
                            )
                        }
                    },
                    visualTransformation = if (viewModel.passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),

                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPasswordState.text ,
                    onValueChange ={
                        viewModel.OnChangeConfirmPassword(it)

                    },
                    trailingIcon = {
                        if(viewModel.passwordVisibility.value){
                            Icon(
                                Icons.Filled.Visibility,
                                contentDescription = "Visibility on",
                                Modifier
                                    .size(26.dp)
                                    .clickable {
                                        viewModel.onChangePasswordVisibility()
                                    }
                            )
                        }else{
                            Icon(
                                Icons.Filled.VisibilityOff,
                                contentDescription = "Visibility Off",
                                Modifier
                                    .size(26.dp)
                                    .clickable {
                                        viewModel.onChangePasswordVisibility()
                                    }
                            )
                        }
                    },
                    visualTransformation = if (viewModel.passwordVisibility.value) VisualTransformation.None else PasswordVisualTransformation(),

                    label = { Text("Confirm Password") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = {
                        viewModel.signUpUser(navController,scaffoldState,context = context)
                    }
                )
                {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(30.dp))
                TextButton(
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    onClick = {
                    navController.navigate(Screens.LOGIN_SCREEN)

                }) {
                    Text(text = "Login")
                }
            }
            if (isLoading){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }
    }
}