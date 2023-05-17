package com.peterchege.blogger.domain.state

import com.peterchege.blogger.core.api.responses.Post
import com.skydoves.sealedx.core.Extensive
import com.skydoves.sealedx.core.annotations.ExtensiveModel
import com.skydoves.sealedx.core.annotations.ExtensiveSealed

@ExtensiveSealed(
    models = [
        ExtensiveModel(FeedScreenUi::class),

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