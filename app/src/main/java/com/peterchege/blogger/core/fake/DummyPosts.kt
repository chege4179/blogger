package com.peterchege.blogger.core.fake


import com.peterchege.blogger.core.api.responses.models.PostAuthor
import com.peterchege.blogger.core.api.responses.models.PostCount
import com.peterchege.blogger.domain.models.PostUI
import java.text.SimpleDateFormat
import java.util.* // For Date

// Assuming you have PostAuthor and PostCount classes defined elsewhere

val dummyPostList = listOf(
    PostUI(
        postId = "1",
        postTitle = "Exploring Kotlin's Features",
        postBody = "Kotlin offers concise syntax, null safety, and functional programming",
        postAuthorId = "author1",
        imageUrl = "https://example.com/kotlin-image.jpg",
        createdAt = getCurrentTimeString(),
        updatedAt = getCurrentTimeString(),
        postAuthor = PostAuthor(
            userId = "1234567",
            email = "peter@gmail.com",
            fullName = "peter",
            username = "peter",
            imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
        ),
        count = PostCount(likes = 5, comments = 2, views = 3),
        isLiked = true
    ),
    PostUI(
        postId = "2",
        postTitle = "Building Dynamic UIs with Jetpack Compose",
        postBody = "Compose simplifies UI development with declarative syntax and state management",
        postAuthorId = "author2",
        imageUrl = "https://example.com/jetpack-compose-image.png",
        createdAt = getCurrentTimeString(),
        updatedAt = getCurrentTimeString(),
        postAuthor = PostAuthor(
            userId = "1234567",
            email = "peter@gmail.com",
            fullName = "peter",
            username = "peter",
            imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
        ),
        count = PostCount(likes = 10, comments = 4, views = 2),
        isSaved = true
    ),
    // Add more posts as needed
)

// Helper function to get current time as a formatted string
fun getCurrentTimeString(): String {
    val currentTime = Date()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(currentTime)
}
