package com.peterchege.blogger.util


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
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