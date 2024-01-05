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
package com.peterchege.blogger.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peterchege.blogger.core.room.converters.PostAuthorConverter
import com.peterchege.blogger.core.room.converters.PostCountConverter
import com.peterchege.blogger.core.room.converters.UserCountConverter
import com.peterchege.blogger.core.room.dao.CachedPostDao
import com.peterchege.blogger.core.room.dao.DraftPostDao
import com.peterchege.blogger.core.room.dao.FollowerDao
import com.peterchege.blogger.core.room.dao.LikeDao
import com.peterchege.blogger.core.room.dao.SavedPostDao
import com.peterchege.blogger.core.room.entities.*

@TypeConverters(
    PostAuthorConverter::class,
    PostCountConverter::class,
    UserCountConverter::class,
)
@Database(
    entities = [
        DraftPost::class,
        SavePost::class,
        CachePost::class,
        FollowerUserEntity::class,
        LikeEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class BloggerDatabase : RoomDatabase() {

    abstract val savedPostDao: SavedPostDao
    abstract val draftPostDao: DraftPostDao
    abstract val cachedPostDao: CachedPostDao
    abstract val followerDao: FollowerDao
    abstract val likeDao: LikeDao


}