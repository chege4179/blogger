package com.peterchege.blogger.domain.mappers

import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.View
import com.peterchege.blogger.core.room.entities.CommentEntity
import com.peterchege.blogger.core.room.entities.LikeEntity
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.room.entities.ViewEntity



fun ViewEntity.toExternalModel(): View {
    return View(
        viewerUsername = viewerUsername,
        viewerFullname = viewerFullname,
        viewerId = viewerId,

    )
}

fun LikeEntity.toExternalModel(): Like {
    return Like(
        username = username,
        id = id,
        userId = userId,
    )
}

fun CommentEntity.toExternalModel():Comment{
    return Comment(
        username = username,
        userId = userId,
        imageUrl = imageUrl,
        commentId = id,
        postedAt = postedAt,
        postedOn = postedOn,
        comment = comment

    )
}

fun PostRecordWithCommentsLikesViews.toExternalModel():Post{
    return Post(
        _id = post._id,
        postTitle = post.postTitle,
        postBody = post.postBody,
        imageUrl = post.ImageUrl,
        postedAt = post.postedAt,
        postAuthor = post.postAuthor,
        postedOn = post.postedOn,
        views = views.map { it.toExternalModel() },
        likes = likes.map { it.toExternalModel() },
        comments = comments.map { it.toExternalModel() },
    )
}