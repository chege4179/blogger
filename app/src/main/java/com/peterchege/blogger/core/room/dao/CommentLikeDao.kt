package com.peterchege.blogger.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peterchege.blogger.core.room.entities.CommentLikeEntity
import com.peterchege.blogger.core.room.entities.LikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentLikeDao {
    @Insert
    suspend fun insertCommentLikes(likes: List<CommentLikeEntity>)

    @Insert
    suspend fun insertCommentLike(like: CommentLikeEntity)

    @Query("SELECT * FROM commentLike")
    fun getAllCommentLikes(): Flow<List<CommentLikeEntity>>

    @Query("DELETE FROM commentLike WHERE likeCommentId =:commentId AND userId =:userId")
    suspend fun deleteCommentLike(commentId:String,userId:String)

    @Query("DELETE FROM commentLike")
    suspend fun deleteAllCommentLikes()

}