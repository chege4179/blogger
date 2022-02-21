package com.peterchege.blogger.ui.dashboard.addpost_screen

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.PostBody
import com.peterchege.blogger.api.responses.UploadPostResponse
import javax.inject.Inject

class AddPostRepository @Inject  constructor(
    private val api: BloggerApi

) {
    suspend fun uploadPost(postBody: PostBody):UploadPostResponse{
        return api.uploadPost(postBody = postBody)
    }

}