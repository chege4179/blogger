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
package com.peterchege.blogger.data.local.posts.saved

import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.domain.mappers.toExternalModel
import com.peterchege.blogger.domain.mappers.toSavedEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject


class SavedPostsDataSourceImpl @Inject constructor(
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    private val db: BloggerDatabase,

    ) : SavedPostsDataSource {

    override suspend fun insertSavedPost(post: Post) = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.insertSavedPost(post.toSavedEntity())

    }

    override suspend fun deleteAllSavedPosts() = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.deleteAllSavedPosts()

    }

    override suspend fun deleteSavedPostById(id: String) = withContext(context = ioDispatcher) {
        return@withContext db.savedPostDao.deleteSavedPostById(id)
    }

    override fun getSavedPostById(postId: String): Flow<Post?> {
        return db.savedPostDao.getSavedPostById(postId)
            .map { it?.toExternalModel() }
            .flowOn(ioDispatcher)
    }


    override fun getAllSavedPosts(): Flow<List<Post>> {
        return db.savedPostDao.getAllSavedPosts().flowOn(ioDispatcher)
            .map { it.map { it.toExternalModel() } }
    }

    override fun getSavedPostIds(): Flow<List<String>> {
        return db.savedPostDao.getSavedPostIds().flowOn(ioDispatcher)
    }

}