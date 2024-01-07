package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun ProfileAvatar(imageUrl:String,modifier: Modifier) {
    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier
            .width(80.dp)
            .height(80.dp),
        contentDescription = "Profile Image"
    )
}