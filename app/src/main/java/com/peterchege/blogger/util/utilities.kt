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
package com.peterchege.blogger.util


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

object ImageResizer {
    //For Image Size 640*480, use MAX_SIZE =  307200 as 640*480 307200
    //private static long MAX_SIZE = 360000;
    //private static long THUMB_SIZE = 6553;
    fun reduceBitmapSize(bitmap: Bitmap, MAX_SIZE: Int): Bitmap {
        val ratioSquare: Double
        val bitmapHeight: Int
        val bitmapWidth: Int
        bitmapHeight = bitmap.height
        bitmapWidth = bitmap.width
        ratioSquare = (bitmapHeight * bitmapWidth / MAX_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = Math.sqrt(ratioSquare)

        val requiredHeight = Math.round(bitmapHeight / ratio).toInt()
        val requiredWidth = Math.round(bitmapWidth / ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }

    fun generateThumb(bitmap: Bitmap, THUMB_SIZE: Int): Bitmap {
        val ratioSquare: Double
        val bitmapHeight: Int
        val bitmapWidth: Int
        bitmapHeight = bitmap.height
        bitmapWidth = bitmap.width
        ratioSquare = (bitmapHeight * bitmapWidth / THUMB_SIZE).toDouble()
        if (ratioSquare <= 1) return bitmap
        val ratio = Math.sqrt(ratioSquare)

        val requiredHeight = Math.round(bitmapHeight / ratio).toInt()
        val requiredWidth = Math.round(bitmapWidth / ratio).toInt()
        return Bitmap.createScaledBitmap(bitmap, requiredWidth, requiredHeight, true)
    }
}

fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
    var width = image.width
    var height = image.height
    val bitmapRatio = width.toFloat() / height.toFloat()
    if (bitmapRatio > 1) {
        width = maxSize
        height = (width / bitmapRatio).toInt()
    } else {
        height = maxSize
        width = (height * bitmapRatio).toInt()
    }
    return Bitmap.createScaledBitmap(image, width, height, true)
}

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

fun compressBitmap(bitmap: Bitmap, quality:Int):Bitmap{
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
    val byteArray = stream.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun hasInternetConnection(context: Context): Boolean {
    var result = false
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
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


