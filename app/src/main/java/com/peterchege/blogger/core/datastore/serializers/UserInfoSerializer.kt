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
package com.peterchege.blogger.core.datastore.serializers

import androidx.datastore.core.Serializer
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.core.api.responses.models.UserCount
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object UserInfoSerializer : Serializer<User?> {

    override val defaultValue: User?
        get() = null

    override suspend fun readFrom(input: InputStream): User? {
        return try {
            Json.decodeFromString(
                deserializer = User.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: User?, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = User.serializer(),
                value = t ?: User(
                    userId = "",
                    fullName = "",
                    email = "",
                    password = "",
                    username = "",
                    imageUrl = "",
                    deviceTokens = emptyList(),
                    createdAt = "",
                    updatedAt = "",
                    count = UserCount(0,0,0),
                    isEmailVerified = false

                )
            ).encodeToByteArray()
        )
    }
}