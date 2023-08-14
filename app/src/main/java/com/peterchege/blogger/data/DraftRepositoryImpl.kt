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
package com.peterchege.blogger.data

import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.core.room.entities.DraftRecord
import com.peterchege.blogger.domain.repository.DraftRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject


class DraftRepositoryImpl @Inject constructor(
    private val db: BloggerDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
):DraftRepository {

    override suspend fun insertDraft(draft: DraftRecord){
        withContext(ioDispatcher){
            db.draftDao.insertDraft(draft)
        }

    }
    override fun getAllDrafts(): Flow<List<DraftRecord>> {
        return db.draftDao.getAllDrafts().flowOn(ioDispatcher)
    }

    override suspend fun getDraftById(id:Int): DraftRecord? {
        return withContext(ioDispatcher){
            db.draftDao.getDraftById(id)
        }
    }

    override suspend fun deleteAllDrafts(){
        withContext(ioDispatcher){
            db.draftDao.deleteAllDrafts()
        }
    }

    override suspend fun deleteDraftById(id: Int){
        withContext(ioDispatcher){
            db.draftDao.deleteDraftById(id)
        }
    }

    override suspend fun updateDraft(
        postTitle: String,
        postBody: String,
        imageUri: String,
        draftId: Int
    ) {
        withContext(ioDispatcher){
            db.draftDao.updateDraft(postTitle, postBody, imageUri, draftId)
        }
    }

}