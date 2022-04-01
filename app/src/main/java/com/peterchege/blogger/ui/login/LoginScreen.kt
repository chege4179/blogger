package com.peterchege.blogger.ui.login

import android.widget.Toast
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.util.Screens
import com.peterchege.blogger.util.UiEvent
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalComposeUiApi
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()

){
    val context = LocalContext.current
    val state = viewModel.state.value
    val usernameState = viewModel.usernameState.value
    val passwordState = viewModel.passwordState.value
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
                    text = "Login ",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = usernameState.text,
                    onValueChange ={
                        viewModel.OnChangeUsername(it)
                        //state.username = it
                    },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordState.text ,
                    onValueChange ={
                        viewModel.OnChangePassword(it)
                        //state.password = it
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
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.initiateLogin(navController,scaffoldState, context = context)
                    }
                )
                {
                    Text("Login")
                }
                Spacer(modifier = Modifier.height(30.dp))
                TextButton(onClick = {
                    navController.navigate(Screens.SIGNUP_SCREEN)

                }) {
                    Text(text = "Sign Up")
                }
            }
            if (viewModel.isLoading.value){
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

        }

    }



}


