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
package com.peterchege.blogger.core.util


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.compose.ui.text.capitalize
import java.io.ByteArrayOutputStream
import java.util.*


//fun ByteArray.toBase64(): String = String(Base64.getEncoder().encode(this))

fun Bitmap.size(): Int{
    val s = rowBytes * height
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
        return try {
            allocationByteCount
        } catch (npe: NullPointerException) {
            s
        }
    }
    return s
}

fun randomColorCode(): String {

    // create random object - reuse this as often as possible
    val random = Random()

    // create a big random number - maximum is ffffff (hex) = 16777215 (dez)
    val nextInt = random.nextInt(0xffffff + 1)

    // format it as hexadecimal string (with hashtag and leading zeros)

    return String.format("#%06x", nextInt).drop(1).capitalize(Locale.ROOT)
}

fun randomID(): String = List(6) {
    (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
}.joinToString("")
fun generateAvatarURL(name:String):String{
    var splitname = name.split(" ").joinToString("+")
    var color = randomColorCode()
    return "https://ui-avatars.com/api/?background=${color}&color=fff&name=${splitname}&bold=true&fontsize=0.6&rounded=true"

}


