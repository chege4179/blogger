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
package com.peterchege.blogger.data.local.posts.cache

import com.peterchege.blogger.core.api.responses.models.Post
import kotlinx.coroutines.flow.Flow

interface CachedPostsDataSource {

    suspend fun insertCachedPosts(posts:List<Post>)

    fun getCachedPosts(): Flow<List<Post>>

    suspend fun deleteAllPostsFromCache()

    fun getCachedPostById(postId:String):Flow<Post?>


}