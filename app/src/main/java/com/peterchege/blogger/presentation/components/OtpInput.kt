package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalTextApi::class, ExperimentalComposeUiApi::class)
@Composable
fun OTPInputs(
    otpInputValue:String,
    otpOnValueChange:(String) -> Unit,
    onSubmitOTP:() -> Unit,
    onCancel:() -> Unit,
) {

    val keyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Characters,
        autoCorrect = false,
        keyboardType = KeyboardType.Number,
        imeAction = ImeAction.Next
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent)
            .padding(top = 10.dp, start = 0.dp)
    ) {
        OutlinedTextField(
            value = otpInputValue,
            onValueChange = otpOnValueChange,
            keyboardOptions = keyboardOptions
        )
    }
}


@Preview
@Composable
fun OTPInputsPreview(){
    OTPInputs(
        otpInputValue = "12345",
        otpOnValueChange = { },
        onSubmitOTP = { /*TODO*/ },
        onCancel = {  }
    )
}
