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

import android.content.Context
import org.json.JSONObject

class Device constructor(context: Context) {

    var releaseBuildVersion: String? = null
    var buildVersionCodeName: String? = null
    var manufacturer: String? = null
    var model: String? = null
    var product: String? = null
    var fingerprint: String? = null
    var hardware: String? = null
    var radioVersion: String? = null
    var device: String? = null
    var board: String? = null
    var displayVersion: String? = null
    var buildBrand: String? = null
    var buildHost: String? = null
    var buildTime: Long = 0
    var buildUser: String? = null
    var serial: String? = null
    var osVersion: String? = null
    var language: String? = null
    var sdkVersion: Int = 0
    var screenDensity: String? = null
    var screenHeight: Int = 0
    var screenWidth: Int = 0

    init {
        val deviceInfo = DeviceInfo(context)
        this.releaseBuildVersion = deviceInfo.releaseBuildVersion
        this.buildVersionCodeName = deviceInfo.buildVersionCodeName
        this.manufacturer = deviceInfo.manufacturer
        this.model = deviceInfo.model
        this.product = deviceInfo.product
        this.fingerprint = deviceInfo.fingerprint
        this.hardware = deviceInfo.hardware
        this.radioVersion = deviceInfo.radioVer
        this.device = deviceInfo.device
        this.board = deviceInfo.board
        this.displayVersion = deviceInfo.displayVersion
        this.buildBrand = deviceInfo.buildBrand
        this.buildHost = deviceInfo.buildHost
        this.buildTime = deviceInfo.buildTime
        this.buildUser = deviceInfo.buildUser
        this.serial = deviceInfo.serial
        this.osVersion = deviceInfo.osVersion
        this.language = deviceInfo.language
        this.sdkVersion = deviceInfo.sdkVersion
        this.screenDensity = deviceInfo.screenDensity
        this.screenHeight = deviceInfo.screenHeight
        this.screenWidth = deviceInfo.screenWidth
    }

    fun toJSON(): JSONObject? {
        try {
            val jsonObject = JSONObject()
            jsonObject.put("releaseBuildVersion", releaseBuildVersion)
            jsonObject.put("buildVersionCodeName", buildVersionCodeName)
            jsonObject.put("manufacturer", manufacturer)
            jsonObject.put("model", model)
            jsonObject.put("product", product)
            jsonObject.put("fingerprint", fingerprint)
            jsonObject.put("hardware", hardware)
            jsonObject.put("radioVersion", radioVersion)
            jsonObject.put("device", device)
            jsonObject.put("board", board)
            jsonObject.put("displayVersion", displayVersion)
            jsonObject.put("buildBrand", buildBrand)
            jsonObject.put("buildHost", buildHost)
            jsonObject.put("buildTime", buildTime)
            jsonObject.put("buildUser", buildUser)
            jsonObject.put("serial", serial)
            jsonObject.put("osVersion", osVersion)
            jsonObject.put("language", language)
            jsonObject.put("sdkVersion", sdkVersion)
            jsonObject.put("screenDensity", screenDensity)
            jsonObject.put("screenHeight", screenHeight)
            jsonObject.put("screenWidth", screenWidth)
            return jsonObject
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
