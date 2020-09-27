package com.masrooraijaz.fireinsta.models

import com.google.firebase.Timestamp

data class Comment(
    val comment: String? = null,
    val postId: String? = null,
    val time: Timestamp? = null,
    val userId: String? = null,
    val username: String? = null
)