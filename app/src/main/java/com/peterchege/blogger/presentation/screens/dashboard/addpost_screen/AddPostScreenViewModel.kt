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

import android.net.Uri
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.core.api.BloggerApi
import com.peterchege.blogger.core.api.requests.PostBody
import com.peterchege.blogger.core.api.responses.User
import com.peterchege.blogger.core.util.*
import com.peterchege.blogger.core.work.upload_post.UploadPostWorkManager
import com.peterchege.blogger.domain.repository.AuthRepository
import com.peterchege.blogger.domain.repository.DraftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

data class AddPostFormState(
    val postTitle:String = "",
    val postBody:String = "",
    val uri:Uri? = null,
    val isLoading: Boolean= false,

)
@HiltViewModel
class AddPostScreenViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val uploadPostWorkManager: UploadPostWorkManager,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val isUploading = uploadPostWorkManager.isUploading
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = false
        )


    private val _formState = MutableStateFlow(AddPostFormState())
    val formState = _formState.asStateFlow()

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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

            }
        }
        postTitleDraft.let {
            if (it != null) {

            }
        }

    }

    private fun loadUser(){
        viewModelScope.launch {
            authRepository.getLoggedInUser().collectLatest {
                _user.value = it
            }
        }
    }


    fun onBackPress(scaffoldState: ScaffoldState, navController: NavController) {
//        if (_imageUrlState.value != null || _bitmapState.value != null ||
//            _postBody.value.text != "" || _postTitle.value.text != ""
//        ) {
//
//            _openSaveDraftModal.value = true
//
//
//        } else {
//            navController.navigate(Screens.DASHBOARD_SCREEN)
//
//
//        }
    }

    fun inputPostBodyFromDraft(postBodyDraft: String?) {
        if (postBodyDraft == null) {

        } else {

        }
    }

    fun inputPostTitleFromDraft(postTitleDraft: String?) {
        if (postTitleDraft == null) {


        } else {


        }
    }

    fun onChangePostTitle(text: String) {
        _formState.value = _formState.value.copy(postTitle = text)

    }

    fun onChangePostBody(text: String) {
        _formState.value = _formState.value.copy(postBody = text)
    }

    fun onChangePhotoUri(uri: Uri?) {
        _formState.value = _formState.value.copy(uri = uri)
    }


    fun onSaveDraftDismiss(navController: NavController) {
        _openSaveDraftModal.value = false
        navController.navigate(Screens.DASHBOARD_SCREEN)
    }

    fun onSaveDraftConfirm(scaffoldState: ScaffoldState, navController: NavController) {
        _openSaveDraftModal.value = false
        viewModelScope.launch {
            try {
//                draftRepository.insertDraft(
//                    DraftRecord(
//                        postTitle = _postTitle.value.text,
//                        postBody = _postBody.value.text
//                    )
//                )
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Your draft has been saved"
                )
                navController.navigate(Screens.DASHBOARD_SCREEN)
            } catch (e: IOException) {

            }
        }
    }


    fun postArticle() {
        viewModelScope.launch {
            val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
            val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
            val username = _user.value?.username ?:""
            val postBody = PostBody(
                postTitle = _formState.value.postTitle,
                postBody = _formState.value.postBody,
                postedBy = username,
                postedOn = postedOn,
                postedAt = postedAt,
                photo = _formState.value.postTitle,
            )

            _formState.value.uri?.let {
                uploadPostWorkManager.startUpload(postBody = postBody,uri = it)
                _eventFlow.emit(UiEvent.Navigate(route = Screens.DASHBOARD_SCREEN))
            }


        }


    }
}