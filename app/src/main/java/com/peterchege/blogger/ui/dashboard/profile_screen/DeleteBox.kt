package com.peterchege.blogger.ui.dashboard.profile_screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.peterchege.blogger.api.requests.CommentBody
import com.peterchege.blogger.api.responses.Post
import com.peterchege.blogger.ui.post_screen.PostViewModel
import com.peterchege.blogger.util.Constants
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DeleteBox(
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,

) {

    val context = LocalContext.current


    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            viewModel.onDialogDeleteDismiss()
        },
        text = {
            Column {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Delete" + "${viewModel.state.value.post?.postTitle}",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Are you sure you want to delete this post ?",
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                val buttonModifier = Modifier.align(Alignment.CenterVertically)
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onDialogDismiss()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colors.onSurface
                    )
                ) {
                    Text(text = "Cancel".uppercase())
                }
                TextButton(
                    modifier = buttonModifier,
                    onClick = {
                        viewModel.onDialogDeleteConfirm(scaffoldState = scaffoldState)

                    }
                ) {
                    Text(text = "Delete".uppercase())
                }
            }
        }
    )
}