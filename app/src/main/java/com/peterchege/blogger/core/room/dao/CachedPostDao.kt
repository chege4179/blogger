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

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.peterchege.blogger.core.room.entities.CachePost
import kotlinx.coroutines.flow.Flow


@Dao
interface CachedPostDao {

    @Query("SELECT * FROM cachePost")
    fun getAllCachedPosts(): Flow<List<CachePost>>


    @Query("SELECT * FROM cachePost WHERE postId = :postId")
    fun getCachedPostById(postId:String):Flow<CachePost?>

    @Query("DELETE FROM cachePost")
    suspend fun deleteAllCachedPosts()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedPost(post:CachePost)
}