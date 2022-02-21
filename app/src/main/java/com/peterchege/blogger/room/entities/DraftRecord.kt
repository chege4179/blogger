package com.peterchege.blogger.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "draft")
data class DraftRecord (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val postTitle:String = "",
    val postBody:String = "",
)