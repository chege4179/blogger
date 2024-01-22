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
package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.peterchege.blogger.R

@Composable
fun ProfileAvatar(imageUrl: String, modifier: Modifier, size: Int) {
    SubcomposeAsyncImage(
        model = imageUrl,
        modifier = modifier
            .width(size.dp)
            .height(size.dp)
            .clip(CircleShape),
        loading = {
            Image(
                painter = painterResource(id = R.mipmap.default_profile),
                contentDescription = stringResource(id = R.string.default_profile_image),
                modifier = Modifier
                    .width(size.dp)
                    .height(size.dp)
            )
        },
        contentDescription = stringResource(id = R.string.profile_image)
    )
}