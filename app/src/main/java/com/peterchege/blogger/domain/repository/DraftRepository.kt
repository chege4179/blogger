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
package com.peterchege.blogger.domain.repository

import com.peterchege.blogger.core.room.entities.DraftPost
import kotlinx.coroutines.flow.Flow

interface DraftRepository {

    suspend fun insertDraft(draft: DraftPost)

    fun getAllDrafts(): Flow<List<DraftPost>>

    suspend fun getDraftById(id:Int): DraftPost?

    suspend fun deleteAllDrafts()

    suspend fun deleteDraftById(id: Int)

    suspend fun updateDraft(postTitle:String,postBody:String,imageUri:String,draftId:Int)
}