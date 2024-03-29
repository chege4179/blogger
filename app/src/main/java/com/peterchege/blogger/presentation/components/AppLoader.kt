package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun AppLoader(isLoading: Boolean) {
    val indicatorSize = 30.dp
    val trackWidth: Dp = (indicatorSize * .1f)
    if (isLoading) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Black, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(0.dp)
                            .size(indicatorSize),
                        strokeWidth = trackWidth,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "Please wait...",
                        modifier = Modifier.padding(0.dp, 8.dp, 0.dp, 0.dp),
                        style = TextStyle(color = Color.White)
                    )

                }
            }
        }
    }

}

@Preview
@Composable
fun AppLoaderPreview() {
    AppLoader(isLoading = true)

}