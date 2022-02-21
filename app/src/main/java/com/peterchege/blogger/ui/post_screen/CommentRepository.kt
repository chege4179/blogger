package com.peterchege.blogger.ui.post_screen

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.requests.CommentBody
import com.peterchege.blogger.api.responses.CommentResponse
import javax.inject.Inject

class CommentRepository @Inject constructor (
    private val api: BloggerApi
) {
    suspend fun postComment(commentBody: CommentBody) : CommentResponse {
        return api.postComment(commentbody = commentBody)
    }

}