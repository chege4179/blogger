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
package com.peterchege.blogger.core.device

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Point
import android.media.AudioManager
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.WifiManager
import android.nfc.NfcAdapter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.RequiresApi
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.util.Locale

private const val BATTERY_HEALTH_COLD = "cold"
private const val BATTERY_HEALTH_DEAD = "dead"
private const val BATTERY_HEALTH_GOOD = "good"
private const val BATTERY_HEALTH_OVERHEAT = "Over Heat"
private const val BATTERY_HEALTH_OVER_VOLTAGE = "Over Voltage"
private const val BATTERY_HEALTH_UNKNOWN = "Unknown"
private const val BATTERY_HEALTH_UNSPECIFIED_FAILURE = "Unspecified failure"

private const val BATTERY_PLUGGED_AC = "Charging via AC"
private const val BATTERY_PLUGGED_USB = "Charging via USB"
private const val BATTERY_PLUGGED_WIRELESS = "Wireless"
private const val BATTERY_PLUGGED_UNKNOWN = "Unknown Source"

private const val RINGER_MODE_NORMAL = "Normal"
private const val RINGER_MODE_SILENT = "Silent"
private const val RINGER_MODE_VIBRATE = "Vibrate"

private const val PHONE_TYPE_GSM = "GSM"
private const val PHONE_TYPE_CDMA = "CDMA"
private const val PHONE_TYPE_NONE = "Unknown"

private const val NETWORK_TYPE_2G = "2G"
private const val NETWORK_TYPE_3G = "3G"
private const val NETWORK_TYPE_4G = "4G"
private const val NETWORK_TYPE_WIFI_WIFIMAX = "WiFi"

private const val NOT_FOUND_VAL = "unknown"

class DeviceInfo(private val context: Context) {

    /* Device Info: */
    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model
            } else {
                "$manufacturer $model"
            }
        }

    val deviceLocale: String?
        get() {
            var locale: String? = null
            val current = context.resources.configuration.locale
            if (current != null) {
                locale = current.toString()
            }

            return locale
        }

    val releaseBuildVersion: String
        get() = Build.VERSION.RELEASE

    val buildVersionCodeName: String
        get() = Build.VERSION.CODENAME

    val manufacturer: String
        get() = Build.MANUFACTURER

    val model: String
        get() = Build.MODEL

    val product: String
        get() = Build.PRODUCT

    val fingerprint: String
        get() = Build.FINGERPRINT

    val hardware: String
        get() = Build.HARDWARE

    val radioVer: String
        @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        get() = Build.getRadioVersion()

    val device: String
        get() = Build.DEVICE

    val board: String
        get() = Build.BOARD

    val displayVersion: String
        get() = Build.DISPLAY

    val buildBrand: String
        get() = Build.BRAND

    val buildHost: String
        get() = Build.HOST

    val buildTime: Long
        get() = Build.TIME

    val buildUser: String
        get() = Build.USER

    val serial: String
        get() = Build.SERIAL

    val osVersion: String
        get() = Build.VERSION.RELEASE

    val language: String
        get() = Locale.getDefault().language

    val sdkVersion: Int
        get() = Build.VERSION.SDK_INT

    val screenDensity: String
        get() {
            val density = context.resources.displayMetrics.densityDpi
            var scrType = ""
            when (density) {
                DisplayMetrics.DENSITY_LOW -> scrType = "ldpi"
                DisplayMetrics.DENSITY_MEDIUM -> scrType = "mdpi"
                DisplayMetrics.DENSITY_HIGH -> scrType = "hdpi"
                DisplayMetrics.DENSITY_XHIGH -> scrType = "xhdpi"
                else -> scrType = "other"
            }
            return scrType
        }

    // deprecated
    val screenHeight: Int
        get() {
            var height = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                height = size.y
            } else {
                height = display.height
            }
            return height
        }

    // deprecated
    val screenWidth: Int
        get() {
            var width = 0
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val display = wm.defaultDisplay
            if (Build.VERSION.SDK_INT > 12) {
                val size = Point()
                display.getSize(size)
                width = size.x
            } else {
                width = display.width
            }
            return width
        }

    /* App Info: */
    val versionName: String?
        get() {
            val pInfo: PackageInfo
            try {
                pInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    0
                )
                return pInfo.versionName
            } catch (e1: Exception) {
                return null
            }
        }

    val versionCode: Int?
        get() {
            val pInfo: PackageInfo
            try {
                pInfo = context.packageManager.getPackageInfo(
                    context.packageName,
                    0
                )
                return pInfo.versionCode
            } catch (e1: Exception) {
                return null
            }
        }

    val packageName: String
        get() = context.packageName

    val activityName: String
        get() = context.javaClass.simpleName

    val appName: String
        get() {
            val packageManager = context.packageManager
            var applicationInfo: ApplicationInfo? = null
            try {
                applicationInfo =
                    packageManager.getApplicationInfo(context.applicationInfo.packageName, 0)
            } catch (e: PackageManager.NameNotFoundException) {
            }

            return (if (applicationInfo != null) packageManager.getApplicationLabel(applicationInfo) else NOT_FOUND_VAL) as String
        }

    /* Battery Info:
     * battery percentage
     * is phone charging at the moment
     * Battery Health
     * Battery Technology
     * Battery Temperature
     * Battery Voltage
     * Charging Source
     * Check if battery is present */
    private val batteryStatusIntent: Intent?
        get() {
            val batFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
            return context.registerReceiver(null, batFilter)
        }

    val batteryPercent: Int
        get() {
            val intent = batteryStatusIntent
            val rawlevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            var level = -1
            if (rawlevel >= 0 && scale > 0) {
                level = rawlevel * 100 / scale
            }
            return level
        }

    val isPhoneCharging: Boolean
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB
        }

    val batteryHealth: String
        get() {
            var health = BATTERY_HEALTH_UNKNOWN
            val intent = batteryStatusIntent
            val status = intent!!.getIntExtra(BatteryManager.EXTRA_HEALTH, 0)
            when (status) {
                BatteryManager.BATTERY_HEALTH_COLD -> health = BATTERY_HEALTH_COLD

                BatteryManager.BATTERY_HEALTH_DEAD -> health = BATTERY_HEALTH_DEAD

                BatteryManager.BATTERY_HEALTH_GOOD -> health = BATTERY_HEALTH_GOOD

                BatteryManager.BATTERY_HEALTH_OVERHEAT -> health = BATTERY_HEALTH_OVERHEAT

                BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> health = BATTERY_HEALTH_OVER_VOLTAGE

                BatteryManager.BATTERY_HEALTH_UNKNOWN -> health = BATTERY_HEALTH_UNKNOWN

                BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE ->
                    health =
                        BATTERY_HEALTH_UNSPECIFIED_FAILURE
            }
            return health
        }

    val batteryTechnology: String
        get() {
            val intent = batteryStatusIntent
            return intent!!.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY).toString()
        }

    val batteryTemperature: Float
        get() {
            val intent = batteryStatusIntent
            val temperature = intent!!.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)
            return (temperature / 10.0).toFloat()
        }

    val batteryVoltage: Int
        get() {
            val intent = batteryStatusIntent
            return intent!!.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0)
        }

    val chargingSource: String
        get() {
            val intent = batteryStatusIntent
            val plugged = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0)
            when (plugged) {
                BatteryManager.BATTERY_PLUGGED_AC -> return BATTERY_PLUGGED_AC
                BatteryManager.BATTERY_PLUGGED_USB -> return BATTERY_PLUGGED_USB
                BatteryManager.BATTERY_PLUGGED_WIRELESS -> return BATTERY_PLUGGED_WIRELESS
                else -> return BATTERY_PLUGGED_UNKNOWN
            }
        }

    val isBatteryPresent: Boolean
        get() {
            val intent = batteryStatusIntent
            return intent!!.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false)
        }

    val isRunningOnEmulator: Boolean
        get() = (
                Build.FINGERPRINT.startsWith("generic") ||
                        Build.FINGERPRINT.startsWith("unknown") ||
                        Build.MODEL.contains("google_sdk") ||
                        Build.MODEL.contains("Emulator") ||
                        Build.MODEL.contains("Android SDK built for x86") ||
                        Build.MANUFACTURER.contains("Genymotion") ||
                        Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic") ||
                        "google_sdk" == Build.PRODUCT ||
                        Build.PRODUCT.contains("vbox86p") ||
                        Build.DEVICE.contains("vbox86p") ||
                        Build.HARDWARE.contains("vbox86")
                )

    val deviceRingerMode: String
        get() {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            when (audioManager.ringerMode) {
                AudioManager.RINGER_MODE_SILENT -> return RINGER_MODE_SILENT
                AudioManager.RINGER_MODE_VIBRATE -> return RINGER_MODE_VIBRATE
                else -> return RINGER_MODE_NORMAL
            }
        }

    val isDeviceRooted: Boolean
        get() {
            val paths = arrayOf(
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su",
                "/system/bin/failsafe/su",
                "/data/local/su",
                "/su/bin/su"
            )
            for (path in paths) {
                if (File(path).exists()) return true
            }
            return false
        }

    val androidId: String
        get() = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

    val installSource: String
        get() {
            val pm = context.packageManager
            return pm.getInstallerPackageName(context.packageName)!!
        }

    val userAgent: String
        get() {
            val systemUa = System.getProperty("http.agent")
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                WebSettings.getDefaultUserAgent(context) + "__" + systemUa
            } else {
                WebView(context).settings.userAgentString + "__" + systemUa
            }
        }

    val gsfId: String
        get() {
            val URI = Uri.parse("content://com.google.android.gsf.gservices")
            val ID_KEY = "android_id"
            val params = arrayOf(ID_KEY)
            val c = context.contentResolver.query(URI, null, null, params, null)

            if (!c!!.moveToFirst() || c.columnCount < 2) {
                c.close()
                return NOT_FOUND_VAL
            }
            try {
                val gsfId = java.lang.Long.toHexString(java.lang.Long.parseLong(c.getString(1)))
                c.close()
                return gsfId
            } catch (e: NumberFormatException) {
                c.close()
                return NOT_FOUND_VAL
            }
        }

    val totalRAM: Long
        get() {
            var totalMemory: Long = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                val mi = ActivityManager.MemoryInfo()
                val activityManager =
                    context.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
                activityManager.getMemoryInfo(mi)
                return mi.totalMem
            }
            try {
                val reader = RandomAccessFile("/proc/meminfo", "r")
                val load = reader.readLine().replace("\\D+".toRegex(), "")
                totalMemory = Integer.parseInt(load).toLong()
                reader.close()
                return totalMemory
            } catch (e: IOException) {
                e.printStackTrace()
                return 0L
            }
        }

    val availableInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long
            val availableBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                availableBlocks = stat.availableBlocksLong
            } else {
                blockSize = stat.blockSize.toLong()
                availableBlocks = stat.availableBlocks.toLong()
            }
            return availableBlocks * blockSize
        }

    val totalInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize: Long
            val totalBlocks: Long
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                blockSize = stat.blockSizeLong
                totalBlocks = stat.blockCountLong
            } else {
                blockSize = stat.blockSize.toLong()
                totalBlocks = stat.blockCount.toLong()
            }
            return totalBlocks * blockSize
        }

    val availableExternalMemorySize: Long
        get() {
            if (hasExternalSDCard()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize: Long
                val availableBlocks: Long
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.blockSizeLong
                    availableBlocks = stat.availableBlocksLong
                } else {
                    blockSize = stat.blockSize.toLong()
                    availableBlocks = stat.availableBlocks.toLong()
                }
                return availableBlocks * blockSize
            }
            return 0
        }

    val totalExternalMemorySize: Long
        get() {
            if (hasExternalSDCard()) {
                val path = Environment.getExternalStorageDirectory()
                val stat = StatFs(path.path)
                val blockSize: Long
                val totalBlocks: Long
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    blockSize = stat.blockSizeLong
                    totalBlocks = stat.blockCountLong
                } else {
                    blockSize = stat.blockSize.toLong()
                    totalBlocks = stat.blockCount.toLong()
                }
                return totalBlocks * blockSize
            }
            return 0
        }

    val phoneType: String
        get() {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            when (tm.phoneType) {
                TelephonyManager.PHONE_TYPE_GSM -> return PHONE_TYPE_GSM
                TelephonyManager.PHONE_TYPE_CDMA -> return PHONE_TYPE_CDMA
                TelephonyManager.PHONE_TYPE_NONE -> return PHONE_TYPE_NONE
                else -> return PHONE_TYPE_NONE
            }
        }

    val operator: String
        get() {
            var operatorName: String?
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            operatorName = telephonyManager.networkOperatorName
            if (operatorName == null) {
                operatorName = telephonyManager.simOperatorName
            }
            return operatorName
        }

    val isSimNetworkLocked: Boolean
        get() {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return telephonyManager.simState == TelephonyManager.SIM_STATE_NETWORK_LOCKED
        }

    val isNfcPresent: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
                return nfcAdapter != null
            }
            return false
        }

    val isNfcEnabled: Boolean
        get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
                return nfcAdapter != null && nfcAdapter.isEnabled
            }
            return false
        }

    val isWifiEnabled: Boolean
        get() {
            val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            return wifiManager.isWifiEnabled
        }

    val isNetworkAvailable: Boolean
        get() {
            val cm =
                context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

    fun isAppInstalled(packageName: String): Boolean {
        return context.packageManager.getLaunchIntentForPackage(packageName) != null
    }

    fun hasExternalSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }
}