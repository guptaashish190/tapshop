package com.tapshop.tapshop.models

class UserModel(
    val username: String = "",
    val profilePicUrl: String = "",
    val gender: String = "",
    val friends: List<String> = emptyList()
)