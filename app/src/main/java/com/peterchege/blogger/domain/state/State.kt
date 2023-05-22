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
package com.peterchege.blogger.domain.state

import com.peterchege.blogger.core.api.requests.Notification
import com.peterchege.blogger.core.api.responses.Follower
import com.peterchege.blogger.core.api.responses.Following
import com.peterchege.blogger.core.api.responses.Post
import com.peterchege.blogger.core.api.responses.User
import com.skydoves.sealedx.core.Extensive
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed

@ExtensiveSealed(
    models = [
        ExtensiveModel(FeedScreenUi::class),
        ExtensiveModel(AuthorProfileScreenUi::class),
        ExtensiveModel(AuthorProfileFollowerFollowingUi::class),
        ExtensiveModel(NotificationScreenUi::class),


    ]
)
sealed interface State {
    data class Success(val data: Extensive) : State
    object Loading : State
    data class Error(val message:String) : State
}

data class FeedScreenUi(
    val posts:List<Post>,
)

data class AuthorProfileScreenUi(
    val posts: List<Post>,
    val user: User? = null
)

data class AuthorProfileFollowerFollowingUi(
    val followers:List<Follower>,
    val following:List<Following>,
)

data class NotificationScreenUi(
    val notifications:List<Notification>
)