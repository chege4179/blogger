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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.Screens
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.presentation.components.CommentBox
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.screens.dashboard.profile_screen.DeleteBox
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PostScreen(

    viewModel: PostScreenViewModel = hiltViewModel()

){
    val authUser by viewModel.authUserFlow.collectAsStateWithLifecycle(initialValue = null)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val commentUiState by viewModel.commentUiState.collectAsStateWithLifecycle()
    val deletePostUiState by viewModel.deletePostUiState.collectAsStateWithLifecycle()

    PostScreenContent(
        uiState = uiState,
        commentUiState = commentUiState,
        deletePostUiState = deletePostUiState,
        onLikePost = viewModel::likePost,
        onUnlikePost = viewModel::unlikePost,
        onFollowUser = viewModel::followUser,
        onUnFollowUser = viewModel::unfollowUser,
        onChangeNewComment = viewModel::onChangeComment,
        savePost = viewModel::savePostToRoom,
        unSavePost = viewModel::deletePostFromRoom,
        openCommentDialog = viewModel::onDialogOpen,
        closeCommentDialog = viewModel::onDialogDismiss,
        openDeleteDialog = viewModel::onDialogDeleteOpen,
        closeDeleteDialog = viewModel::onDialogDeleteDismiss,
        eventFlow = viewModel.eventFlow,
        user = authUser,
        postComment = {
            viewModel.onDialogConfirm(authUser!!)
        }
    )
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun PostScreenContent(
    uiState:PostScreenUiState,
    commentUiState: CommentUiState,
    deletePostUiState: DeletePostUiState,
    onLikePost:(User,String) -> Unit,
    onUnlikePost:(User, String) -> Unit,
    onFollowUser:(User,String) -> Unit,
    onUnFollowUser:(User,String) -> Unit,
    onChangeNewComment:(String) -> Unit,
    savePost:(Post) -> Unit,
    unSavePost:(String) -> Unit,
    openCommentDialog:() -> Unit,
    closeCommentDialog:() -> Unit,
    openDeleteDialog:() -> Unit,
    closeDeleteDialog:() -> Unit,
    eventFlow:SharedFlow<UiEvent>,
    user: User?,
    postComment:() -> Unit,
) {

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openCommentDialog()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Message,
                    contentDescription = "Add a comment"
                )
            }
        }
    ) {
        when(uiState){
            is PostScreenUiState.Loading -> {
                LoadingComponent()
            }
            is PostScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = "An unexpected error occurred")
            }
            is PostScreenUiState.PostNotFound -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = "Post not found")
            }
            is PostScreenUiState.Success -> {
                val post = uiState.post
                if (deletePostUiState.isDeleteDialogOpen) {
                    DeleteBox(
                        post = post.toPost(),
                        closeDeleteDialog = { closeDeleteDialog() },
                        deletePost = {  },
                        )
                }
                if (commentUiState.isCommentDialogOpen) {
                    CommentDialog(
                        closeCommentDialog = { closeCommentDialog() },
                        commentUiState = commentUiState,
                        postComment = { postComment() },
                        onChangeNewComment = { onChangeNewComment(it) },
                    )
                }
                LazyColumn() {
                    item {
                        Box(
                            Modifier.fillMaxSize()
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                SubcomposeAsyncImage(
                                    model = post.imageUrl,
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
                                        text = post.postTitle,
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
                                            text = post.postAuthor,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {
//                                                navController.navigate(Screens.AUTHOR_PROFILE_SCREEN + "/${state.post.postAuthor}")
                                            }
                                        )
                                        if (post.isLiked) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = "Un Like",
                                                modifier = Modifier.clickable {
                                                    if (user != null) {
                                                        onUnlikePost(user,post.postAuthor)
                                                    }
                                                },
                                                tint = Color.Red
                                            )
                                        } else {
                                            Icon(
                                                imageVector = Icons.Default.FavoriteBorder,
                                                contentDescription = "Like",
                                                modifier = Modifier.clickable {
                                                    if (user != null) {
                                                        onLikePost(user,post.postAuthor)
                                                    }
                                                }
                                            )
                                        }
                                        Icon(
                                            imageVector = if (post.isSaved)
                                                Icons.Default.Bookmark
                                            else
                                                Icons.Default.Bookmark,
                                            contentDescription = if(post.isSaved) "Un Save" else "Save",
                                            modifier = Modifier.clickable {
                                                if(post.isSaved){
                                                    unSavePost(post._id)
                                                }else{
                                                    savePost(post.toPost())
                                                }
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Share,
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
                                        Text(text = post.postedOn)
                                        Text(text = post.postedAt)
                                        Text(text = "${post.likes.size} like(s)")
                                        Text(text = "${post.views.size} view(s)")

                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                }
                                Divider(
                                    color = Color.Blue,
                                    thickness = 1.dp,
                                    modifier = Modifier.padding(10.dp)
                                )
                                Text(
                                    text = post.postBody,
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
                                    text = "Comments (${post.comments.size})",
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
                    items(items = commentUiState.comments) { comment ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp)
                        ) {
                            CommentBox(comment = comment)
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
    commentUiState:CommentUiState,
    onChangeNewComment: (String) -> Unit,
    postComment:() -> Unit,
    closeCommentDialog: () -> Unit,
) {


    val context = LocalContext.current


    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            closeCommentDialog()
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
                    value = commentUiState.newComment,
                    onValueChange = {
                        onChangeNewComment(it)
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
                        closeCommentDialog()
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
                        closeCommentDialog()
                    }
                ) {
                    Text(text = "Comment".uppercase())
                }
            }
        }
    )
}
