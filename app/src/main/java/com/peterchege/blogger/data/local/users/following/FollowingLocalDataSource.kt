package com.peterchege.blogger.data.local.users.following

import com.peterchege.blogger.core.api.responses.models.FollowerUser
import kotlinx.coroutines.flow.Flow

interface FollowingLocalDataSource {

    suspend fun insertFollowing(followerUser: FollowerUser)

    suspend fun insertFollowings(followers:List<FollowerUser>)

    fun getAllAuthUserFollowings(): Flow<List<FollowerUser>>

    fun getAllAuthUserFollowingsIds(): Flow<List<String>>

    suspend fun deleteFollowingByUserId(userId:String)


    suspend fun deleteAllFollowing()
}