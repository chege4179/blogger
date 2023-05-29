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
package com.peterchege.blogger.data.local.posts.cached_posts

import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.domain.mappers.toExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CachedPostsDataSourceImpl @Inject constructor(
    private val db:BloggerDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) :CachedPostsDataSource{
    override suspend fun insertCachedPosts(posts: List<Post>)  {
        withContext(ioDispatcher){
            posts.map {
                db.cachedPostDao.insertPostCache(post = it)
            }
        }

    }
    override fun getCachedPosts(): Flow<List<Post>> {
        return db.cachedPostDao.getAllLocalPosts().map { it.map { it.toExternalModel() } }
    }

    override suspend fun deleteAllPostsFromCache() {
        withContext(ioDispatcher){
            db.cachedPostDao.deleteAllPostsFromCache()
        }
    }

}