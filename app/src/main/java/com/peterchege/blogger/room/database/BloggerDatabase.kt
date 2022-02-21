package com.peterchege.blogger.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.blogger.room.dao.DraftDao
import com.peterchege.blogger.room.dao.PostDao
import com.peterchege.blogger.room.entities.DraftRecord
import com.peterchege.blogger.room.entities.PostRecord

@Database(
    entities = [PostRecord::class,DraftRecord::class],
    version = 1
)
abstract class BloggerDatabase: RoomDatabase() {

    abstract val postDao: PostDao
    abstract val draftDao:DraftDao


}