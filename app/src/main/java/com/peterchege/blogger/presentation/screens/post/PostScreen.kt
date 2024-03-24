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
package com.peterchege.blogger.presentation.screens.post


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.R
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import com.peterchege.blogger.core.api.responses.models.Post
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.fake.dummyPostList
import com.peterchege.blogger.core.util.UiEvent
import com.peterchege.blogger.core.util.calculateDoubleTapOffset
import com.peterchege.blogger.core.util.calculateNewOffset
import com.peterchege.blogger.core.util.convertUtcDateStringToReadable
import com.peterchege.blogger.core.util.toast
import com.peterchege.blogger.domain.mappers.toPost
import com.peterchege.blogger.presentation.alertDialogs.CommentDialog
import com.peterchege.blogger.presentation.alertDialogs.DeleteCommentDialog
import com.peterchege.blogger.presentation.alertDialogs.ReplyCommentDialog
import com.peterchege.blogger.presentation.components.AppBackgroundImage
import com.peterchege.blogger.presentation.components.ErrorComponent
import com.peterchege.blogger.presentation.components.LoadingComponent
import com.peterchege.blogger.presentation.components.postCommentsSection
import com.peterchege.blogger.presentation.navigation.scaleInEnterTransition
import com.peterchege.blogger.presentation.navigation.scaleOutExitTransition
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoilApi::class)
@Composable
fun PostScreen(
    viewModel: PostScreenViewModel = hiltViewModel()
) {

    val authUser by viewModel.authUserFlow.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val commentUiState by viewModel.commentUiState.collectAsStateWithLifecycle()

    val context = LocalContext.current


    PostScreenContent(
        uiState = uiState,
        commentUiState = commentUiState,
        onLikePost = { it1 ->
            if (authUser == null) {
                context.toast(msg = context.getString(R.string.login_like_message))
            }
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.likePost(post = it1, user = user)
                } else {
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        onUnlikePost = { it1 ->
            authUser?.let { user ->
                if (user.userId != "") {
                    viewModel.unLikePost(post = it1, user = user)
                } else {
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        onChangeNewComment = viewModel::onChangeComment,
        savePost = viewModel::savePostToRoom,
        unSavePost = viewModel::deletePostFromRoom,
        openCommentDialog = viewModel::onCommentDialogOpen,
        closeCommentDialog = viewModel::onCommentDialogDismiss,
        eventFlow = viewModel.eventFlow,
        user = authUser,
        toggleDeleteCommentDialog = viewModel::toggleDeleteCommentDialog,
        postComment = {
            viewModel.onCommentDialogConfirm(authUser!!, it)
        },
        deleteComment = viewModel::deleteComment,
        setCommentToBeDeleted = viewModel::setCommentToBeDeleted,
        setCommentToRepliedTo = viewModel::setCommentToBeRepliedTo,
        toggleReplyCommentDialog = viewModel::toggleReplyCommentDialog,
        addCommentParticipants = viewModel::addCommentParticipants,
        onChangeReplyComment = viewModel::onChangeReplyComment,
        replyToComment = { refresh ->
            authUser?.let { user ->

                if (user.userId != "") {
                    if (uiState is PostScreenUiState.Success) {
                        viewModel.replyToComment(
                            postAuthorId = (uiState as PostScreenUiState.Success).post.postAuthorId,
                            userId = user.userId,
                            refresh = refresh
                        )
                    }
                } else {
                    context.toast(msg = context.getString(R.string.login_like_message))
                }
            }
        },
        likeComment = viewModel::likeComment
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalCoilApi
@Composable
fun PostScreenContent(
    uiState: PostScreenUiState,
    commentUiState: CommentUiState,
    toggleDeleteCommentDialog: () -> Unit,
    onLikePost: (Post) -> Unit,
    onUnlikePost: (Post) -> Unit,
    onChangeNewComment: (String) -> Unit,
    savePost: (Post) -> Unit,
    unSavePost: (String) -> Unit,
    openCommentDialog: () -> Unit,
    closeCommentDialog: () -> Unit,
    eventFlow: SharedFlow<UiEvent>,
    user: User?,
    postComment: (() -> Unit) -> Unit,
    deleteComment: (() -> Unit) -> Unit,
    setCommentToBeDeleted: (CommentWithUser) -> Unit,
    onChangeReplyComment: (String) -> Unit,
    toggleReplyCommentDialog: () -> Unit,
    setCommentToRepliedTo: (CommentWithUser?) -> Unit,
    addCommentParticipants: (List<String>) -> Unit,
    replyToComment: (() -> Unit) -> Unit,
    likeComment: (String, String, () -> Unit) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val commentsBottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(key1 = true) {
        eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UiEvent.Navigate -> {

                }
            }
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    openCommentDialog()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = stringResource(id = R.string.add_comment_description)
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            is PostScreenUiState.Loading -> {
                LoadingComponent()
            }

            is PostScreenUiState.Error -> {
                ErrorComponent(
                    retryCallback = { },
                    errorMessage = "An unexpected error occurred"
                )
            }

            is PostScreenUiState.PostNotFound -> {
                ErrorComponent(
                    retryCallback = { /*TODO*/ },
                    errorMessage = stringResource(id = R.string.no_posts_found)
                )
            }

            is PostScreenUiState.Success -> {
                val post = uiState.post
                val comments = uiState.comments.collectAsLazyPagingItems()

                if (commentUiState.isReplyCommentDialogVisible && (commentUiState.commentToBeRepliedTo != null)) {
                    ReplyCommentDialog(
                        isUserLoggedIn = uiState.isUserLoggedIn,
                        commentUiState = commentUiState,
                        onChangeNewComment = onChangeReplyComment,
                        replyToComment = {
                            replyToComment { comments.refresh() }
                        },
                        closeCommentDialog = toggleReplyCommentDialog
                    )
                }

                if (commentUiState.isDeleteCommentDialogVisible && (commentUiState.commentToBeDeleted != null)) {
                    DeleteCommentDialog(
                        comment = commentUiState.commentToBeDeleted,
                        closeDeleteDialog = toggleDeleteCommentDialog,
                        deleteComment = {
                            deleteComment { comments.refresh() }
                        }
                    )
                }

                AnimatedVisibility(
                    visible = commentUiState.isCommentDialogVisible,
                    enter = scaleInEnterTransition(),
                    exit = scaleOutExitTransition()
                ) {
                    CommentDialog(
                        closeCommentDialog = { closeCommentDialog() },
                        commentUiState = commentUiState,
                        postComment = { postComment { comments.refresh() } },
                        onChangeNewComment = { onChangeNewComment(it) },
                        isUserLoggedIn = uiState.isUserLoggedIn,
                    )
                }
                LazyColumn(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxSize()
                        )
                        {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                var offset by remember { mutableStateOf(Offset.Zero) }
                                var zoom by remember { mutableStateOf(1f) }
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
                                        .height(300.dp)
                                        .clipToBounds()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onDoubleTap = { tapOffset ->
                                                    zoom = if (zoom > 1f) 1f else 2f
                                                    offset = calculateDoubleTapOffset(
                                                        zoom = zoom,
                                                        size = size,
                                                        tapOffset = tapOffset
                                                    )

                                                }
                                            )
                                        }
                                        .pointerInput(Unit) {
                                            detectTransformGestures(
                                                onGesture = { centroid, pan, gestureZoom, _ ->
                                                    offset = offset.calculateNewOffset(
                                                        centroid = centroid,
                                                        pan = pan,
                                                        zoom = zoom,
                                                        gestureZoom = gestureZoom,
                                                        size = size
                                                    )
                                                    zoom = maxOf(a = 1f, b = zoom * gestureZoom)
                                                }
                                            )
                                        }
                                        .graphicsLayer {
                                            translationX = -offset.x * zoom
                                            translationY = -offset.y * zoom
                                            scaleX = zoom
                                            scaleY = zoom
                                            transformOrigin = TransformOrigin(
                                                pivotFractionX = 0f,
                                                pivotFractionY = 0f
                                            )
                                        },
                                    contentDescription = stringResource(id = R.string.post_image)
                                )

                                Spacer(
                                    modifier = Modifier
                                        .height(8.dp)
                                        .padding(15.dp)
                                )
                                HorizontalDivider(
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
                                            text = post.postAuthor.fullName,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.clickable {}
                                        )
                                        Icon(
                                            imageVector = if (post.isLiked)
                                                Icons.Default.Favorite
                                            else
                                                Icons.Default.FavoriteBorder,
                                            contentDescription = if (post.isLiked)
                                                stringResource(id = R.string.unlike_description)
                                            else
                                                stringResource(id = R.string.like_description),
                                            modifier = Modifier.clickable {
                                                if (user != null) {
                                                    if (post.isLiked) {
                                                        onUnlikePost(post.toPost())
                                                    } else {
                                                        onLikePost(post.toPost())
                                                    }

                                                }
                                            }
                                        )
                                        Icon(
                                            imageVector = if (post.isSaved)
                                                Icons.Default.Bookmark
                                            else
                                                Icons.Default.BookmarkBorder,
                                            contentDescription = if (post.isSaved)
                                                stringResource(id = R.string.un_save_description)
                                            else
                                                stringResource(id = R.string.save_description),
                                            modifier = Modifier.clickable {
                                                if (post.isSaved) {
                                                    unSavePost(post.postId)
                                                } else {
                                                    savePost(post.toPost())
                                                }
                                            }
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = stringResource(id = R.string.share_description),
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
                                        Text(text = convertUtcDateStringToReadable(post.createdAt))

                                        Text(text = "${post.count.likes} like(s)")
                                        Text(text = "${post.count.views} view(s)")

                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(8.dp)
                                            .padding(15.dp)
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(10.dp),
                                    thickness = 1.dp,
                                    color = Color.Blue
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
                            }
                        }
                    }
                    postCommentsSection(
                        comments = comments,
                        postAuthorId = post.postAuthorId,
                        authUser = user,
                        toggleDeleteCommentDialog = toggleDeleteCommentDialog,
                        setCommentToBeDeleted = setCommentToBeDeleted,
                        setCommentToBeRepliedTo = setCommentToRepliedTo,
                        toggleReplyCommentDialog = toggleReplyCommentDialog,
                        addParticipants = addCommentParticipants,
                        likeComment = { commentId, userId ->
                            likeComment(commentId, userId, { comments.refresh() })
                        }
                    )
                }
            }

            else -> {}
        }
    }
}

@Preview
@Composable
fun PostScreenContentPreview() {

    PostScreenContent(
        uiState = PostScreenUiState.Success(
            post = dummyPostList[0],
            isUserLoggedIn = false,
            comments = flowOf()
        ),
        commentUiState = CommentUiState(),
        toggleDeleteCommentDialog = { /*TODO*/ },
        onLikePost = {},
        onUnlikePost = {},
        onChangeNewComment = {},
        savePost = {},
        unSavePost = {},
        openCommentDialog = { /*TODO*/ },
        closeCommentDialog = { /*TODO*/ },
        eventFlow = MutableSharedFlow(),
        user = null,
        postComment = {},
        deleteComment = {},
        setCommentToBeDeleted = {},
        onChangeReplyComment = {},
        toggleReplyCommentDialog = { /*TODO*/ },
        setCommentToRepliedTo = {},
        addCommentParticipants = {},
        replyToComment = {},
        likeComment = {_,_,_ ->}
    )
}
