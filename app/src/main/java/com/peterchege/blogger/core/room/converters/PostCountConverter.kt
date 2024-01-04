package com.peterchege.blogger.core.room.converters

import androidx.room.TypeConverter
import com.peterchege.blogger.core.api.responses.models.PostCount
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PostCountConverter {
    @TypeConverter
    fun toString(postCount: PostCount):String{
        return Json.encodeToString<PostCount>(postCount)
    }
    @TypeConverter
    fun fromString(postCountString:String): PostCount {
        return Json.decodeFromString<PostCount>(postCountString)
    }
}