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
package com.peterchege.blogger.domain.mappers

import com.peterchege.blogger.core.api.responses.Comment
import com.peterchege.blogger.core.api.responses.Like
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.View
import com.peterchege.blogger.core.room.entities.CommentCacheEntity
import com.peterchege.blogger.core.room.entities.CommentEntity
import com.peterchege.blogger.core.room.entities.LikeCacheEntity
import com.peterchege.blogger.core.room.entities.LikeEntity
import com.peterchege.blogger.core.room.entities.PostCacheRecordWithCommentsLikesViews
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import com.peterchege.blogger.core.room.entities.ViewCacheEntity
import com.peterchege.blogger.core.room.entities.ViewEntity
import com.peterchege.blogger.domain.models.PostUI


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
        fullname = fullname,
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


fun ViewCacheEntity.toExternalModel(): View {
    return View(
        viewerUsername = viewerUsername,
        viewerFullname = viewerFullname,
        viewerId = viewerId,

        )
}

fun LikeCacheEntity.toExternalModel(): Like {
    return Like(
        username = username,
        fullname = fullname,
        userId = userId,
    )
}

fun CommentCacheEntity.toExternalModel():Comment{
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

fun Post.toDomain(
    isLiked:Boolean,
    isProfile:Boolean,
    isSaved:Boolean,
): PostUI {
    return PostUI(
        _id = _id,
        postTitle = postTitle,
        postBody = postBody,
        imageUrl = imageUrl,
        postedAt = postedAt,
        postAuthor = postAuthor,
        postedOn = postedOn,
        views = views,
        likes = likes,
        comments = comments,
        isLiked = isLiked,
        isProfile = isProfile,
        isSaved = isSaved
    )
}
fun PostUI.toPost():Post{
    return Post(
        _id = _id,
        postTitle = postTitle,
        postBody = postBody,
        imageUrl = imageUrl,
        postedAt = postedAt,
        postAuthor = postAuthor,
        postedOn = postedOn,
        views = views,
        likes = likes,
        comments = comments,
    )
}

fun PostCacheRecordWithCommentsLikesViews.toExternalModel():Post{
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