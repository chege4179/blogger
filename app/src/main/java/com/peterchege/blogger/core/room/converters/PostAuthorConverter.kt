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