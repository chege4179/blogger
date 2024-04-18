package com.peterchege.blogger.core.fake

import com.peterchege.blogger.core.api.responses.models.Notification
import com.peterchege.blogger.core.api.responses.models.PostAuthor
import java.util.UUID

val dummyNotifications = (1..20).map {
    Notification(
        notificationId = UUID.randomUUID().toString(),
        senderId = UUID.randomUUID().toString(),
        recieverId = UUID.randomUUID().toString(),
        createdAt = "2023-12-02T18:55:36.935Z",
        updatedAt = "2023-12-02T18:55:36.935Z",
        notificationSender = PostAuthor(
            userId = "1234567",
            email = "peter@gmail.com",
            fullName = "peter",
            username = "peter",
            imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
            password = "2023-12-02T18:55:36.935Z",
        ),
        notificationReceiver = PostAuthor(
            userId = "1234567",
            email = "peter@gmail.com",
            fullName = "peter",
            username = "peter",
            imageUrl = "https://ui-avatars.com/api/?background=719974&color=fff&name=Peter+Chege&bold=true&fontsize=0.6",
            createdAt = "2023-12-02T18:55:36.935Z",
            updatedAt = "2023-12-02T18:55:36.935Z",
            password = "2023-12-02T18:55:36.935Z",
        ),
        notificationPostId = UUID.randomUUID().toString(),
        notificationCommentId = null,
        notificationContent =if (it % 2 == 0) "Peter has commented on your post" else "Peter has liked your post" ,
        notificationType = if (it % 2 == 0) "Comment" else "Like"
    )
}