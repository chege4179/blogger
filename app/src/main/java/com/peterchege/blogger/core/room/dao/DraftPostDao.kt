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
package com.peterchege.blogger.core.room.dao

import androidx.room.*
import com.peterchege.blogger.core.room.entities.DraftPost
import kotlinx.coroutines.flow.Flow


@Dao
interface DraftPostDao {
    @Query("SELECT * FROM draftPost")
    fun getAllDrafts(): Flow<List<DraftPost>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: DraftPost)

    @Query("SELECT * FROM draftPost where id = :id")
    suspend fun getDraftById(id:Int): DraftPost?

    @Query("DELETE FROM draftPost where id = :id")
    suspend fun deleteDraftById(id:Int)

    @Query("DELETE FROM draftPost")
    suspend fun deleteAllDrafts()

    @Query("UPDATE draftPost SET postTitle =:postTitle, postBody=:postBody , imageUri = :imageUri WHERE id =:draftId")
    suspend fun updateDraft(postTitle:String,postBody:String,imageUri:String,draftId:Int)






}