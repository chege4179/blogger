package com.peterchege.blogger.core.fake

import com.peterchege.blogger.core.api.responses.models.Comment
import com.peterchege.blogger.core.api.responses.models.CommentCount
import com.peterchege.blogger.core.api.responses.models.CommentUser
import com.peterchege.blogger.core.api.responses.models.CommentWithUser
import java.util.UUID

val dummyComments = (1..20).map {
    CommentWithUser(
        commentId = UUID.randomUUID().toString(),
        message = "Dummy Comment ${it}",
        commentPostId = UUID.randomUUID().toString(),
        commentUserId = UUID.randomUUID().toString(),
        createdAt = "",
        updatedAt = "",
        parentId = null,
        count = CommentCount(commentLikes = it),
        children = emptyList(),
        user = CommentUser(
            userId = "1234567",
            email = "peter@gmail.com",
            fullName = "peter",
            username = "peter",
            imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
            password = "2023-12-02T18:55:36.935Z",

        )
    )
}