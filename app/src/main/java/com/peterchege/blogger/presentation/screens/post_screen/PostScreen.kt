/*
 * Copyright 2023 Blogger
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.peterchege.blogger.presentation.screens.post_screen


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Message
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.presentation.components.CommentBox
import com.peterchege.blogger.presentation.screens.dashboard.profile_screen.DeleteBox

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun PostScreen(
    navController: NavController,
    viewModel: PostScreenViewModel = hiltViewModel()

) {
    val state = viewModel.state.value
    val openDialogState = viewModel.openDialogState.value
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onDialogOpen()
                }
            ) {
                Icon(Icons.Outlined.Message, "")
            }
        }
    ) {
        if (viewModel.openDeleteDialogState.value) {
            DeleteBox(scaffoldState = scaffoldState)
        }
        if (openDialogState) {
            CommentDialog(scaffoldState = scaffoldState)
        }
        if (state.error != "") {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(text = state.error)
            }
        } else {
            LazyColumn(

            ) {
                item {
                    Box(
                        Modifier.fillMaxSize()
                    )
                    {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            state.post?.let {
                                SubcomposeAsyncImage(
                                    model = state.post.imageUrl,
                                    loading = {
                                        Box(modifier = Modifier.fillMaxSize()) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.align(
                                                    Alignment.Center
                                                )
                                            )
                                        }
                                    },
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .height(300.dp),
                                    contentDescription = "Post Image"
                                )
//                                Image(
//                                    painter = rememberImagePainter(
//                                        data = state.post.imageUrl,
//                                        builder = {
//                                            crossfade(true)
//
//                                        }
//                                    ),
//                                    contentDescription = "Post Image",
//                                    contentScale = ContentScale.Crop,
//                                    modifier = Modifier
//                                        .fillMaxWidth()
//                                        .height(300.dp)
//                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .padding(15.dp)
                                )
                                Divider(
                                    color = Color.Blue,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Column(
                                    modifier = Modifier.padding(10.dp)

                                ) {
                                    Text(
                                        text = state.post.postTitle,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 30.sp
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                        Arrangement.SpaceEvenly
                                    ) {
                                        Text(text = "By: ")
                                        Text(
                                            text = state.post.postAuthor,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
                                                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${state.post.postAuthor}")
                                            }
                                        )
                                        if (viewModel.isMyPost.value) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Delete",
                                                modifier = Modifier.clickable {
                                                    viewModel.onDialogDeleteOpen()

                                                },

                                                )
                                        }
                                        if (viewModel.isLiked.value) {
                                            Icon(
                                                Icons.Default.Favorite,
                                                contentDescription = "Un Like",
                                                modifier = Modifier.clickable {
                                                    viewModel.unlikePost(scaffoldState)

                                                },
                                                tint = Color.Red
                                            )
                                        } else {
                                            Icon(
                                                Icons.Default.FavoriteBorder,
                                                contentDescription = "Like",
                                                modifier = Modifier.clickable {
                                                    viewModel.likePost(scaffoldState)
                                                }
                                            )
                                        }
                                        if (viewModel.source.value == "api") {
                                            if (viewModel.isSaved.value) {
                                                Icon(
                                                    Icons.Default.Bookmark,
                                                    contentDescription = "Un Save",
                                                    modifier = Modifier.clickable {
                                                        viewModel.deletePostFromRoom(scaffoldState)

                                                    }
                                                )
                                            } else {
                                                Icon(
                                                    Icons.Default.BookmarkBorder,
                                                    contentDescription = "UnSave",
                                                    modifier = Modifier.clickable {

                                                        viewModel.savePostToRoom(scaffoldState)
                                                    }
                                                )
                                            }
                                        } else {
                                            Icon(
                                                Icons.Default.Bookmark,
                                                contentDescription = "Un Save",
                                                modifier = Modifier.clickable {
                                                    viewModel.deletePostFromRoom(scaffoldState)

                                                }
                                            )
                                        }

                                        Icon(
                                            Icons.Default.Share,
                                            contentDescription = "Share",
                                            modifier = Modifier.clickable {

                                            }
                                        )
                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp),
                                        Arrangement.SpaceEvenly,
                                        Alignment.CenterVertically

                                    ) {
                                        Text(text = state.post.postedOn)
                                        Text(text = state.post.postedAt)
                                        Text(text = "${state.post.likes.size} like(s)")
                                        Text(text = "${state.post.views.size} view(s)")

                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.Center,
                                    ) {
                                        if (viewModel.source.value == "room") {
                                            Text(text = "Offline Mode")
                                        }
                                    }
                                }
                                Divider(
                                    color = Color.Blue,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Text(
                                    text = state.post.postBody,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .padding(10.dp)

                                )
                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .padding(15.dp)
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 15.dp),
                                    text = "Comments (${state.post.comments.size})",
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 27.sp
                                )
                                Divider(
                                    color = Color.Blue,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(10.dp)
                                )

                            }

                        }
                    }
                }

                state.post?.let { it1 ->
                    items(viewModel.comments.value) { comment ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        ) {
                            CommentBox(comment = comment, navController = navController)
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun CommentDialog(
    modifier: Modifier = Modifier,
    viewModel: PostScreenViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState,
) {
    val commentInputState = viewModel.commentInputState.value
    val commentResponseState = viewModel.commentResponseState.value
    val context = LocalContext.current


    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            viewModel.onDialogDismiss()
        },
        text = {
            Column {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Comment....",
                        style = MaterialTheme.typography.h6,
                        textAlign = TextAlign.Center
                    )
                }
                TextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = commentInputState,
                    onValueChange = {
                        viewModel.OnChangeComment(it)
                    },
                    singleLine = false,
                    textStyle = MaterialTheme.typography.h6.copy(textAlign = TextAlign.Start),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {

                        }
                    )
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
                        viewModel.onDialogConfirm(scaffoldState = scaffoldState)
                        if (commentResponseState.isLoading) {
                            Toast.makeText(context, commentResponseState.msg, Toast.LENGTH_LONG)
                                .show()
                        }
                        if (commentResponseState.success) {
                            Toast.makeText(context, commentResponseState.msg, Toast.LENGTH_LONG)
                                .show()
                        }


                    }
                ) {
                    Text(text = "Comment".uppercase())
                }
            }
        }
    )
}
