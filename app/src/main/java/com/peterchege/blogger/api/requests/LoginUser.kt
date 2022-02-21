package com.peterchege.blogger.api.requests


data class LoginUser(
    var username :String = "",
    var password:String = "",
    var token:String = ""
)
