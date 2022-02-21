package com.peterchege.blogger.ui.dashboard.home_screen.feed_screen

import com.peterchege.blogger.api.BloggerApi
import com.peterchege.blogger.api.responses.Post
import javax.inject.Inject

class FeedRepository @Inject constructor (
    private val api: BloggerApi
    ) {
    suspend fun getFeedPosts():List<Post>{
        return api.getAllPosts().posts
    }


}