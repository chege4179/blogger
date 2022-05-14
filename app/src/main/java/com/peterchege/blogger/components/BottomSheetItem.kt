package com.peterchege.blogger.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomSheetItem(
    name:String,
    onClick:() -> Unit,


) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .height(60.dp)
            .clickable{
                onClick()
            }
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start

    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp),
            text = name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 21.sp
        )
    }


}