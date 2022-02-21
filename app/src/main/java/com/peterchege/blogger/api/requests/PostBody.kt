package com.peterchege.blogger.api.requests

import android.graphics.Bitmap
import android.net.Uri
import java.io.File

data class PostBody (
    val postTitle:String,
    val postBody:String,
    val postedBy:String,
    val postedAt:String,
    val postedOn:String,
    val photo:String,


)