package com.peterchege.blogger.room.dao

import androidx.room.*
import com.peterchege.blogger.room.entities.DraftRecord


@Dao
interface DraftDao {
    @Query("SELECT * FROM draft")
    suspend fun getAllDrafts():List<DraftRecord>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDraft(draft: DraftRecord)

    @Query("SELECT * FROM draft where id = :id")
    suspend fun getDraftById(id:Int):DraftRecord

    @Query("DELETE FROM draft where id = :id")
    suspend fun deleteDraftById(id:Int)

    @Query("DELETE FROM draft")
    suspend fun deleteAllDrafts()






}