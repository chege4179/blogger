package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpComponent(
    onSubmitClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    otpInputState: String,
    otpEmailSentTo: String,
    inValidOtpMessage: String? = null,
    onOtpInputStateChange: (String) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .height(300.dp)
            .fillMaxWidth()
            .pointerInput(Unit){
                detectTapGestures {
                    keyboardController?.hide()
                }
            }
    ) {
//               otp content goes here below
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "OTP Verification",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Enter the verification code sent to",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = otpEmailSentTo,
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                ),
            )
            Spacer(modifier = Modifier.height(0.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(1f),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    OTPInputs(
                        otpInputValue = otpInputState,
                        otpOnValueChange = onOtpInputStateChange,
                        onSubmitOTP = onSubmitClicked,
                        onCancel = onCancelClicked,
                    )
                }
            }
            if (inValidOtpMessage != null) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = inValidOtpMessage,
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    ),
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            ClickableText(
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    textAlign = TextAlign.Center
                ),
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 16.sp,

                            ),
                    ) {
                        if (inValidOtpMessage != null) append("Invalid code  ") else append(
                            "Code expired?  "
                        )
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                            textDecoration = TextDecoration.Underline
                        ),
                    ) {
                        append("Resend Code")
                    }
                }, onClick = {
                    println("Clicked offset $it")
                })
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,

                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
            ) {
                Button(
                    onClick = onCancelClicked,
                    modifier = Modifier
                        .height(70.dp)
                        .padding(
                            start = 0.dp,
                            top = 15.dp,
                            end = 0.dp,
                            bottom = 10.dp
                        ),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(10),
                    contentPadding = ButtonDefaults.ContentPadding

                ) {
                    Text(
                        text = "CANCEL",
                        style = TextStyle(
                            color = Color(0xFF707070),
                            fontSize = 17.sp,
                        )
                    )
                }
                Button(
                    onClick = onSubmitClicked,
                    modifier = Modifier
                        .height(70.dp)
                        .padding(
                            start = 0.dp,
                            top = 15.dp,
                            end = 0.dp,
                            bottom = 10.dp
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.inversePrimary
                    ), shape = RoundedCornerShape(18),
                    contentPadding = ButtonDefaults.ContentPadding,
                    enabled = otpInputState.length == 6

                ) {
                    Text(
                        text = "SUBMIT",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun OtpComponentPreview() {
    OtpComponent(
        onCancelClicked = {},
        onSubmitClicked = {},
        otpEmailSentTo = "peterkagure@gmail.com",
        inValidOtpMessage = null,
        onOtpInputStateChange = { _ -> },
        otpInputState = "123455",

        )
}