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
package com.peterchege.blogger.data.local.users

import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.di.IoDispatcher
import com.peterchege.blogger.core.room.database.BloggerDatabase
import com.peterchege.blogger.domain.mappers.toFollower
import com.peterchege.blogger.domain.mappers.toFollowerEntity
import com.peterchege.blogger.domain.mappers.toUser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FollowersLocalDataSourceImpl @Inject constructor(
    private val db: BloggerDatabase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,

) : FollowersLocalDataSource {
    override suspend fun insertFollower(followerUser: FollowerUser) {
        withContext(ioDispatcher) {
            db.followerDao.insertFollower(follower = followerUser.toUser().toFollowerEntity())
        }
    }

    override suspend fun insertFollowers(followers: List<FollowerUser>) {
        withContext(ioDispatcher) {
            db.followerDao.insertFollowers(followers = followers.map {
                it.toUser().toFollowerEntity()
            })
        }
    }

    override fun getAllAuthUserFollowers(): Flow<List<FollowerUser>> {
        return db.followerDao.getAllFollowers()
            .map { it.map { it.toFollower() } }
            .flowOn(ioDispatcher)
    }

    override fun getAllAuthUserFollowerIds(): Flow<List<String>> {
        return db.followerDao.getAllFollowerIds()
            .flowOn(ioDispatcher)
    }

    override suspend fun deleteFollowerByUserId(userId: String) {
       withContext(ioDispatcher){
           db.followerDao.deleteFollower(userId = userId)
       }
    }

    override suspend fun deleteAllFollowers() {
        withContext(ioDispatcher){
            db.followerDao.deleteAllFollowers()
        }
    }


}