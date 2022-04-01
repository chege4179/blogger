package com.peterchege.blogger.ui.dashboard.addpost_screen

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.peterchege.blogger.api.requests.PostBody
import com.peterchege.blogger.room.entities.DraftRecord
import com.peterchege.blogger.ui.dashboard.draft_screen.DraftRepository
import com.peterchege.blogger.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@HiltViewModel
class AddPostScreenViewModel @Inject constructor(
    private val addPostUseCase: AddPostUseCase,
    private val sharedPreferences: SharedPreferences,
    private val draftRepository: DraftRepository,
    private val savedStateHandle: SavedStateHandle,

):ViewModel() {
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

    private val _uiEvent =  Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val  _isContent = mutableStateOf(false)
    var isContent:State<Boolean> = _isContent

    private val  _openSaveDraftModal = mutableStateOf(false)
    var openSaveDraftModal:State<Boolean> = _openSaveDraftModal

    init {
        val postBodyDraft = savedStateHandle.get<String>("postTitle")
        val postTitleDraft = savedStateHandle.get<String>("postBody")
        postBodyDraft.let {
            if (it != null) {
                _postBody.value.text = it
            }
        }
        postTitleDraft.let {
            if (it != null){
                _postTitle.value.text = it
            }
        }

    }

    data class AddPostScreenState(
        val isLoading: Boolean = false,
        val msg:String = "",
        val success:Boolean= false,
    )
    fun onBackPress(scaffoldState: ScaffoldState,navController: NavController){
        if (_imageUrlState.value != null || _bitmapState.value != null ||
            _postBody.value.text != "" || _postTitle.value.text != ""){

            _openSaveDraftModal.value = true


        }else{
            navController.navigate(Screens.DASHBOARD_SCREEN)


        }
    }
    fun inputPostBodyFromDraft(postBodyDraft:String?){
        if (postBodyDraft ==null){
            postBody.value.text = ""

        }else{
            postBody.value.text = postBodyDraft

        }
    }

    fun inputPostTitleFromDraft(postTitleDraft:String?){
        if (postTitleDraft ==null){
            postTitle.value.text = ""

        }else{
            postTitle.value.text = postTitleDraft

        }
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }


    fun onChangePostTitle(text:String){
        _postTitle.value = TextFieldState(text = text)
    }
    fun onChangePostBody(text:String){
        _postBody.value = TextFieldState(text = text)
    }
    fun onChangePhotoUri(uri: Uri?,context: Context){
        if (uri != null){
            if (Build.VERSION.SDK_INT < 28){
                Log.e("Less than API 28","Less than API 28 ")
                onChangePhotoBitmap(MediaStore.Images.Media.getBitmap(context.contentResolver,uri))

            }else{
                Log.e("More than API  28","More than API  28")
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                val decodedBitmap = ImageDecoder.decodeBitmap(source)

                onChangePhotoBitmap(decodedBitmap)

            }
        }
        _imageUrlState.value = uri
    }
    fun onChangePhotoBitmap(bitmap: Bitmap){
        val compresedbitmap = compressBitmap(ImageResizer.reduceBitmapSize(bitmap,100000),100)
        _bitmapState.value = bitmap
    }
    fun onSaveDraftDismiss(navController: NavController){
        _openSaveDraftModal.value = false
        navController.navigate(Screens.DASHBOARD_SCREEN)
    }
    data class InternalStoragePhoto(
        val name: String,
        val bmp: Bitmap
    )
    fun onSaveDraftConfirm(scaffoldState: ScaffoldState,navController: NavController){
        _openSaveDraftModal.value = false
        viewModelScope.launch {
            try {
                draftRepository.insertDraft(DraftRecord(
                    postTitle = _postTitle.value.text,
                    postBody = _postBody.value.text
                ))
                scaffoldState.snackbarHostState.showSnackbar(
                    message = "Your draft has been saved"
                )
                navController.navigate(Screens.DASHBOARD_SCREEN)
            }catch (e:IOException){

            }
        }
    }

    fun postArticle(navController: NavController,scaffoldState: ScaffoldState,context: Context){
        _state.value = AddPostScreenState(isLoading = true)

        if (_postBody.value.text === "" || _postTitle.value.text ==="" || _bitmapState.value === null){
            _state.value = AddPostScreenState(isLoading = false, msg="Please fill in all the fields")

        }else{
            if(hasInternetConnection(context = context)){
                val postedOn = SimpleDateFormat("dd/MM/yyyy").format(Date())
                val postedAt = SimpleDateFormat("hh:mm:ss").format(Date())
                val postBody = PostBody(
                    postTitle = _postTitle.value.text,
                    postBody = _postBody.value.text,
                    postedBy = sharedPreferences.getString(Constants.LOGIN_USERNAME,null)!!,
                    postedOn = postedOn,
                    postedAt = postedAt,
                    photo = _bitmapState.value!!.toByteArray().toBase64()
                )
                addPostUseCase(postBody = postBody).onEach { result ->
                    when(result){
                        is Resource.Success -> {
                            _state.value = AddPostScreenState(
                                msg = result.data!!.msg,
                                isLoading = false,
                                success = result.data.success
                            )
                            if (result.data.success){
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = result.data.msg
                                )
                                navController.navigate(Screens.DASHBOARD_SCREEN)
                            }

                            sendUiEvent(UiEvent.ShowSnackbar(
                                message = result.data!!.msg
                            ))

                        }
                        is Resource.Error -> {
                            Log.d("error","error")
                            _state.value = AddPostScreenState(
                                msg = result.message ?: "An error occurred"
                            )
                            sendUiEvent(UiEvent.ShowSnackbar(
                                message = result.message ?: "An error occurred"
                            ))

                        }
                        is Resource.Loading -> {
                            Log.d("loading","loading")
                            _state.value = AddPostScreenState(isLoading = true)


                        }
                    }

                }.launchIn(viewModelScope)
            }else{
                viewModelScope.launch {
                    _state.value = AddPostScreenState(isLoading = false)
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = "No internet connection was found.... Please check your internet Connection"
                    )
                }
            }

        }

    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }







}