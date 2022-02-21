package com.peterchege.blogger.room.dao

import androidx.room.*
import com.peterchege.blogger.room.entities.PostRecord

import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {

    @Query("SELECT * FROM post")
    fun getPosts(): Flow<List<PostRecord>>

    @Query("SELECT * FROM post WHERE _id = :id")
    suspend fun getPostById(id: String): PostRecord?



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(postRecord: PostRecord)


    @Query("DELETE FROM post WHERE _id = :id")
    suspend fun deletePostById(id: String)

    @Query("DELETE FROM post")
    suspend fun deleteAllPosts()
}