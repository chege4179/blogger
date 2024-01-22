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
package com.peterchege.blogger.data.local.users.following

import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.domain.mappers.toFollower
import com.peterchege.blogger.domain.mappers.toFollowerEntity
import com.peterchege.blogger.domain.mappers.toFollowingEntity
import com.peterchege.blogger.domain.mappers.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FollowingLocalDataSourceImpl @Inject constructor(
    private val db: BloggerDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : FollowingLocalDataSource {
    override suspend fun insertFollowing(followerUser: FollowerUser) {
        withContext(ioDispatcher) {
            db.followingDao.insertFollowing(followerUser.toUser().toFollowingEntity())
        }
    }

    override suspend fun insertFollowings(followers: List<FollowerUser>) {
        withContext(ioDispatcher) {
            db.followingDao.insertFollowings(followers = followers.map {
                it.toUser().toFollowingEntity()
            })
        }
    }

    override fun getAllAuthUserFollowings(): Flow<List<FollowerUser>> {
        return db.followingDao.getAllFollowings().map {
            it.map { it.toFollower() }
        }.flowOn(ioDispatcher)
    }

    override fun getAllAuthUserFollowingsIds(): Flow<List<String>> {
        return db.followingDao.getAllFollowingIds().flowOn(ioDispatcher)
    }

    override suspend fun deleteFollowingByUserId(userId: String) {
        withContext(ioDispatcher){
            db.followingDao.deleteFollowing(userId = userId)
        }
    }

    override suspend fun deleteAllFollowing() {
        withContext(ioDispatcher){
            db.followingDao.deleteAllFollowers()
        }
    }

}