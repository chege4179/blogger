/*
 * Copyright 2024 Blogger
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
import androidx.room.Query
import com.peterchege.blogger.core.room.entities.FollowerUserEntity
import com.peterchege.blogger.core.room.entities.FollowingUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowingDao {

    @Query("SELECT * FROM followings")
    fun getAllFollowings(): Flow<List<FollowingUserEntity>>

    @Query("SELECT userId FROM followings")
    fun getAllFollowingIds(): Flow<List<String>>

    @Insert
    suspend fun insertFollowings(followers:List<FollowingUserEntity>)

    @Insert
    suspend fun insertFollowing(follower: FollowingUserEntity)

    @Query("DELETE FROM followings WHERE userId =:userId")
    suspend fun deleteFollowing(userId:String)

    @Query("DELETE FROM followers")
    suspend fun deleteAllFollowers()




}