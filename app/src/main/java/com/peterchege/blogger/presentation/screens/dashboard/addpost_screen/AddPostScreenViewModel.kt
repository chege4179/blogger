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
package com.peterchege.blogger.presentation.screens.dashboard.addpost_screen

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.work.*
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.room.entities.DraftRecord
import com.peterchege.blogger.core.util.*
import com.peterchege.blogger.data.DraftRepositoryImpl
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.DraftRepository
import com.peterchege.blogger.domain.use_case.AddPostUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddPostScreenViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val authRepository: AuthRepository,
    private val draftRepository: DraftRepository,
    private val savedStateHandle: SavedStateHandle,
    private val api: BloggerApi,
) : ViewModel() {

    private var _user = MutableStateFlow<User?>(null)
    var user: StateFlow<User?> = _user


    private var _imageUrlState = mutableStateOf<Uri?>(null)
    var imageUrlState: State<Uri?> = _imageUrlState

    private var _bitmapState = mutableStateOf<Bitmap?>(null)
    var bitmapState: State<Bitmap?> = _bitmapState

    private var _postTitle = mutableStateOf(TextFieldState())
    var postTitle: State<TextFieldState> = _postTitle

    private var _postBody = mutableStateOf(TextFieldState())
    var postBody: State<TextFieldState> = _postBody

    private var _state = mutableStateOf(AddPostScreenState())
    var state: State<AddPostScreenState> = _state

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _isContent = mutableStateOf(false)
    var isContent: State<Boolean> = _isContent

    private val _openSaveDraftModal = mutableStateOf(false)
    var openSaveDraftModal: State<Boolean> = _openSaveDraftModal


    init {
        loadUser()
        val postBodyDraft = savedStateHandle.get<String>("postTitle")
        val postTitleDraft = savedStateHandle.get<String>("postBody")
        postBodyDraft.let {
            if (it != null) {
                _postBody.value.text = it
            }
        }
        postTitleDraft.let {
            if (it != null) {
                _postTitle.value.text = it
            }
        }

    }

    data class AddPostScreenState(
        val isLoading: Boolean = false,
        val msg: String = "",
        val success: Boolean = false,
    )
    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }


    fun onBackPress(scaffoldState: ScaffoldState, navController: NavController) {
        if (_imageUrlState.value != null || _bitmapState.value != null ||
            _postBody.value.text != "" || _postTitle.value.text != ""
        ) {

            _openSaveDraftModal.value = true


        } else {
            navController.navigate(Screens.DASHBOARD_SCREEN)


        }
    }

    fun inputPostBodyFromDraft(postBodyDraft: String?) {
        if (postBodyDraft == null) {
            postBody.value.text = ""

        } else {
            postBody.value.text = postBodyDraft

        }
    }

    fun inputPostTitleFromDraft(postTitleDraft: String?) {
        if (postTitleDraft == null) {
            postTitle.value.text = ""

        } else {
            postTitle.value.text = postTitleDraft

        }
    }

    fun onChangePostTitle(text: String) {
        _postTitle.value = TextFieldState(text = text)
    }

    fun onChangePostBody(text: String) {
        _postBody.value = TextFieldState(text = text)
    }

    fun onChangePhotoUri(uri: Uri?, context: Context) {
        if (uri != null) {
            if (Build.VERSION.SDK_INT < 28) {
                Log.e("Less than API 28", "Less than API 28 ")
                onChangePhotoBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver, uri))

            } else {
                Log.e("More than API  28", "More than API  28")
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                val decodedBitmap = ImageDecoder.decodeBitmap(source)

                onChangePhotoBitmap(decodedBitmap)

            }
        }
        _imageUrlState.value = uri
    }

    private fun updateUserProfile(
        uri: Uri,
        postBody: PostBody,
        context: Context,
        scaffoldState: ScaffoldState,
        navController: NavController
    ) {
        val file = UriToFile(context = context).getImageBody(uri)
        val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val builder: MultipartBody.Builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder
            .addFormDataPart("postTitle", postBody.postTitle)
            .addFormDataPart("postBody", postBody.postBody)
            .addFormDataPart("postedBy", postBody.postedBy)
            .addFormDataPart("postedAt", postBody.postedAt)
            .addFormDataPart("postedOn", postBody.postedOn)
            .addFormDataPart("photo", file.name, requestFile)

        val requestBody: RequestBody = builder.build()

        viewModelScope.launch {
            try {
                val response = api.postImage(body = requestBody)
                _state.value = AddPostScreenState(
                    msg = response.msg,
                    isLoading = false,
                    success = response.success
                )
                if (response.success) {
                    navController.navigate(Screens.DASHBOARD_SCREEN)
                }
                scaffoldState.snackbarHostState.showSnackbar(
                    message = response.msg
                )
            } catch (e: HttpException) {
                _state.value = AddPostScreenState(
                    msg = "Could not reach server at the moment",
                    isLoading = false,
                    success = false
                )
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Server down....(HTTP error)"
                )
            } catch (e: IOException) {
                _state.value = AddPostScreenState(
                    msg = "The server is down ....Please try again later",
                    isLoading = false,
                    success = false
                )
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "The server is down ....Please try again later"
                )
            }
        }


    }

    private fun onChangePhotoBitmap(bitmap: Bitmap) {
        val compresedbitmap = compressBitmap(ImageResizer.reduceBitmapSize(bitmap, 100000), 100)
        _bitmapState.value = bitmap
    }

    fun onSaveDraftDismiss(navController: NavController) {
        _openSaveDraftModal.value = false
        navController.navigate(Screens.DASHBOARD_SCREEN)
    }

    fun onSaveDraftConfirm(scaffoldState: ScaffoldState, navController: NavController) {
        _openSaveDraftModal.value = false
        viewModelScope.launch {
            try {
                draftRepository.insertDraft(
                    DraftRecord(
                        postTitle = _postTitle.value.text,
                        postBody = _postBody.value.text
                    )
                )
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Your draft has been saved"
                )
                navController.navigate(Screens.DASHBOARD_SCREEN)
            } catch (e: IOException) {

            }
        }
    }


    fun postArticle(navController: NavController, scaffoldState: ScaffoldState, context: Context) {
        _state.value = AddPostScreenState(isLoading = true)

        if (_postBody.value.text === "" || _postTitle.value.text === "" || _bitmapState.value === null) {
            _state.value =
                AddPostScreenState(isLoading = false, msg = "Please fill in all the fields")

        } else {
            val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
            val username = _user.value?.username ?:""
            val postBody = PostBody(
                postTitle = _postTitle.value.text,
                postBody = _postBody.value.text,
                postedBy = username,
                postedOn = postedOn,
                postedAt = postedAt,
                photo = _postTitle.value.text,
            )
            val postArticleParams = workDataOf(
                "uri" to _imageUrlState.value!!.toString(),
                "postBody" to _postBody.value.text,
                "postTitle" to _postTitle.value.text,
                "postedBy" to username,
                "postedAt" to postedAt,
                "photo" to _postTitle.value.text,
                "postedOn" to postedOn
            )
            val postArticleRequest = OneTimeWorkRequestBuilder<UploadPostWorker>()
                .setInputData(postArticleParams)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            NetworkType.CONNECTED
                        )
                        .build()
                )
                .build()
            val workManager = WorkManager.getInstance(context)
            workManager
                .beginUniqueWork(
                    "post_article",
                    ExistingWorkPolicy.KEEP,
                    postArticleRequest
                )
                .enqueue()

            _state.value = AddPostScreenState(
                msg = "Posting",
                isLoading = false,
                success = false
            )
            navController.navigate(Screens.DASHBOARD_SCREEN)
        }
    }
}