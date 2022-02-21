package com.peterchege.blogger.ui.signup

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.peterchege.blogger.util.Screens

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
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = usernameState.text ,
                    onValueChange ={
                        viewModel.OnChangeUsername(it)

                    },
                    label = { Text("Username") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = fullnameState.text ,
                    onValueChange ={
                        viewModel.OnChangeFullName(it)

                    },
                    label = { Text("Full Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailState.text ,
                    onValueChange ={
                        viewModel.OnChangeEmail(it)

                    },
                    label = { Text("Email Address") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordState.text ,
                    onValueChange ={
                        keyboardController?.hide()
                        viewModel.OnChangePassword(it)

                    },
                    label = { Text("Password") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPasswordState.text ,
                    onValueChange ={
                        viewModel.OnChangeConfirmPassword(it)

                    },
                    label = { Text("Confirm Password") }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        viewModel.signUpUser(navController,scaffoldState)
                    }
                )
                {
                    Text("Sign Up")
                }
                Spacer(modifier = Modifier.height(30.dp))
                TextButton(onClick = {
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