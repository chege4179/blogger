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
package com.peterchege.blogger.core.room.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.peterchege.blogger.core.api.responses.PostAuthor
import com.peterchege.blogger.core.api.responses.PostCount


@Entity(tableName = "cachePost")
data class CachePost(
    @PrimaryKey
    val postId: String,
    val postTitle: String,
    val postBody:String,
    val postAuthorId:String,
    val imageUrl: String,
    val createdAt: String,
    val updatedAt: String,
    val postAuthor: PostAuthor,
    val _count: PostCount
)
