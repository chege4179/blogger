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
package com.peterchege.blogger.core.room.converters

import androidx.room.TypeConverter
import com.peterchege.blogger.core.api.responses.models.PostAuthor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostAuthorConverter {

    @TypeConverter
    fun toString(postAuthor: PostAuthor):String{
        return Json.encodeToString<PostAuthor>(postAuthor)
    }
    @TypeConverter
    fun fromString(postAuthorString:String): PostAuthor {
        return Json.decodeFromString<PostAuthor>(postAuthorString)
    }
}