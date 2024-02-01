package com.peterchege.blogger.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NotificationFilterCard(
    filterType: String,
    isActive: Boolean,
    setActiveFilter: (String) -> Unit,
) {
    AssistChip(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .animateContentSize(),
        onClick = {
            setActiveFilter(filterType)
        },
        label = {
            Text(text = filterType)
        },
        trailingIcon = {
            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Filter is active"
                )
            }
        }
    )
}