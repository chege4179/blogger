package com.peterchege.blogger.ui.theme


import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.peterchege.blogger.R

//val fonts = FontFamily(
//    Font(R.font.lato_black),
//    Font(R.font.lato_bold,weight = FontWeight.Bold),
//    Font(R.font.lato_light,weight = FontWeight.Light),
//    Font(R.font.lato_thin,weight = FontWeight.Thin),
//    Font(R.font.lato_italic,weight = FontWeight.Normal, style = FontStyle.Italic),
//)
// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(

        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Light,
        fontSize = 16.sp
    ),
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Thin,
        fontSize = 16.sp
    ),
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontStyle = FontStyle.Italic,
        fontSize = 16.sp
    ),

    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
