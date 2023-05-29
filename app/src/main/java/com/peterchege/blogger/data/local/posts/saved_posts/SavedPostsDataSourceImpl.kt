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
package com.peterchege.blogger.data.local.posts.saved_posts

import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.core.room.entities.PostRecordWithCommentsLikesViews
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SavedPostsDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db:BloggerDatabase,

):SavedPostsDataSource {

    override suspend fun insertPost(post: Post) = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.insertPost(post)

    }

    override suspend fun deleteAllPosts() = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.deleteAllPosts()

    }

    override suspend fun deletePostById(id: String) = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.deletePostById(id)
    }

    override suspend fun getPostFromRoom(postId: String): PostRecordWithCommentsLikesViews? =
        withContext(context = ioDispatcher) {
            return@withContext db.savedPostDao.getPostById(postId)
        }

    override fun getAllPostsFromRoom(): Flow<List<PostRecordWithCommentsLikesViews>> {
        return db.savedPostDao.getAllLocalPosts()
    }

}