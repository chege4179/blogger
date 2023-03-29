/*
 * Copyright 2023 Blogger
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
package com.peterchege.blogger.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peterchege.blogger.room.dao.DraftDao
import com.peterchege.blogger.room.dao.PostDao
import com.peterchege.blogger.room.entities.DraftRecord
import com.peterchege.blogger.room.entities.PostRecord

@Database(
    entities = [PostRecord::class,DraftRecord::class],
    version = 1,
    exportSchema = true
)
abstract class BloggerDatabase: RoomDatabase() {

    abstract val postDao: PostDao
    abstract val draftDao:DraftDao


}