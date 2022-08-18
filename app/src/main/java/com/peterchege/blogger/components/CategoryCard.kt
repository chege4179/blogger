package com.peterchege.blogger.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.peterchege.blogger.util.Screens
import com.peterchege.blogger.util.categoryItem


@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    navController: NavController,
    categoryItem: categoryItem,
    icon: ImageVector? = null
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(5.dp),
            )
            .padding(6.dp)
            .clickable{
                navController.navigate(Screens.CATEGORY_SCREEN + "/${categoryItem.name}")

            }
    ) {
        Text(
            text = categoryItem.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp
        )
        if(icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black
            )
        }
    }
}