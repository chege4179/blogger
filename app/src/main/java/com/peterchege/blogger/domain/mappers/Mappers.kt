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

import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.room.entities.CachePost
import com.peterchege.blogger.core.room.entities.SavePost
import com.peterchege.blogger.domain.models.PostUI

fun Post.toSavedEntity(): SavePost {
    return SavePost(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        postAuthorId = postAuthorId,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        _count = _count
    )

}


fun Post.toCacheEntity(): CachePost {
    return CachePost(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        postAuthorId = postAuthorId,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        _count = _count
    )
}

fun CachePost.toExternalModel():Post {
    return Post(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        postAuthorId = postAuthorId,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        _count = _count
    )
}

fun SavePost.toExternalModel():Post {
    return Post(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        postAuthorId = postAuthorId,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        _count = _count
    )
}

fun Post.toDomain(
    isLiked: Boolean,
    isProfile: Boolean,
    isSaved: Boolean,
): PostUI {
    return PostUI(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        postAuthorId = postAuthorId,
        _count = _count,
        isLiked = isLiked,
        isProfile = isProfile,
        isSaved = isSaved
    )
}

fun PostUI.toPost(): Post {
    return Post(
        postId = postId,
        postTitle = postTitle,
        postBody = postBody,
        imageUrl = imageUrl,
        createdAt = createdAt,
        updatedAt = updatedAt,
        postAuthor = postAuthor,
        postAuthorId = postAuthorId,
        _count = _count,
    )
}
