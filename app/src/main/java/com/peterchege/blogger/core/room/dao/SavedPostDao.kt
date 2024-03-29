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
import com.peterchege.blogger.core.room.entities.*

import kotlinx.coroutines.flow.Flow

@Dao
interface SavedPostDao {


    @Query("SELECT * FROM savePost")
    fun getAllSavedPosts(): Flow<List<SavePost>>


    @Query("SELECT * FROM savePost WHERE postId = :id")
    fun getSavedPostById(id: String): Flow<SavePost?>

    @Query("SELECT postId FROM savePost")
    fun getSavedPostIds():Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedPost(post:SavePost)

    @Query("DELETE FROM savePost WHERE postId = :id")
    suspend fun deleteSavedPostById(id: String)

    @Query("DELETE FROM savePost")
    suspend fun deleteAllSavedPosts()




}