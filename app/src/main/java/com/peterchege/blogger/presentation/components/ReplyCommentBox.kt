package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.ReplyCommentWithUser
import com.peterchege.blogger.core.api.responses.models.User

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalCoilApi
@Composable
fun ReplyCommentBox(
    postAuthorId:String,
    comment: ReplyCommentWithUser,
    authUser: User?,
    openDeleteCommentDialog: () -> Unit,
    setCommentToBeDeleted: (CommentWithUser) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {

                    },
                    onLongClick = {
                        if (authUser != null && authUser.userId != "") {
                            if (postAuthorId == authUser.userId) {
//                                setCommentToBeDeleted(comment)
                                openDeleteCommentDialog()
                            }
                        }
                    }
                ),
        ) {
            Image(
                modifier = Modifier
                    .padding(5.dp)
                    .width(40.dp)
                    .height(40.dp)
                    .clip(CircleShape),
                painter = rememberImagePainter(
                    data = comment.user.imageUrl,
                    builder = {
                        crossfade(true)
                    },
                ),
                contentDescription = "Image of ${comment.user.fullName}"
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                val annotatedText = buildAnnotatedString {
                    pushStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, fontSize = 16.sp))
                    append(comment.user.username + " ")
                    pop()
                    append(comment.message)
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                ) {
                    Text(
                        text = annotatedText,
                        textAlign = TextAlign.Start,
                        textDecoration = TextDecoration.None,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CustomIconButton(
                    imageVector = Icons.Default.FavoriteBorder,
                    onClick = {
                        TODO("implement like comment")
                    },
                    contentDescription = stringResource(id = R.string.like_comment_description)
                )
                Text(
                    text = comment.count.commentLikes.toString()
                )
            }


        }

    }

}
