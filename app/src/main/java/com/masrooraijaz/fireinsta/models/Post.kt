package com.masrooraijaz.fireinsta.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.Exclude

class Post {


    @Exclude
    var postId: String? = null
    var username: String? = null
    var storageFileName: String? = null
    var userId: String? = null
    var caption: String? = null
    var likes: Int? = 0
    var uploadedAt: Timestamp? = null
}