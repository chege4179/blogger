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
package com.peterchege.blogger.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.peterchege.blogger.core.api.responses.models.FollowerUser
import com.peterchege.blogger.core.api.responses.models.User
import com.peterchege.blogger.domain.mappers.toUser
import com.peterchege.blogger.R

@Composable
fun FollowingList(
    followings: LazyPagingItems<FollowerUser>,
    navigateToAuthorProfileScreen:(String) -> Unit,
    paddingValues: PaddingValues,
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(10.dp)
    ){
        if (followings.itemCount == 0){
            item {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(id = R.string.no_following))

                }
            }
        }else{
            items(count = followings.itemCount){ position ->
                val following = followings[position]
                if (following != null) {
                    FollowingCard(
                        navigateToFollowingPage = navigateToAuthorProfileScreen,
                        following =following.toUser(),
                        unFollowUser ={
                        },
                        isYourProfile = false
                    )
                }
            }
        }
    }

}