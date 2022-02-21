package com.peterchege.blogger.api.requests

data class Notification(
    val notificationType:String,
    val notificationSender:String,
    val notificationReceiver:String,
    val notificationContent:String,
    val notificationPostedAt:String? = null,
    val notificationPostedOn:String? = null,
    val notificationImageUrl:String? = null,
    val postId:String? = null
)