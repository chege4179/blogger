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