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
package com.peterchege.blogger.core.api.responses

import com.peterchege.blogger.core.room.entities.PostRecord
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val _id: String,
    val postTitle: String,
    val postBody:String,
    val postAuthor:String,
    val imageUrl: String,
    val postedAt: String,
    val postedOn: String,
    val comments:List<Comment>,
    val views:List<View>,
    val likes:List<Like>
    )

fun Post.toPostRecord(): PostRecord {
    return PostRecord(
        _id,
        postTitle,
        postBody,
        imageUrl,
        postedAt,
        postAuthor,
        postedOn,
    )
}