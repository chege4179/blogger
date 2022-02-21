package com.peterchege.blogger.ui.dashboard.draft_screen

import com.peterchege.blogger.room.dao.DraftDao
import com.peterchege.blogger.room.database.BloggerDatabase
import com.peterchege.blogger.room.entities.DraftRecord
import javax.inject.Inject


class DraftRepository @Inject constructor(
    private val db:BloggerDatabase
) {

    suspend fun insertDraft(draft: DraftRecord){
        return db.draftDao.insertDraft(draft)
    }
    suspend fun getAllDrafts():List<DraftRecord>{
        return db.draftDao.getAllDrafts()
    }

    suspend fun getDraftById(id:Int):DraftRecord{
        return db.draftDao.getDraftById(id)
    }

    suspend fun deleteAllDrafts(){
        return db.draftDao.deleteAllDrafts()
    }

    suspend fun deleteDraftById(id: Int){
        return db.draftDao.deleteDraftById(id)
    }

}