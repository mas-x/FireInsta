package com.masrooraijaz.fireinsta.models

import android.os.Parcelable
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Post(
    @Exclude
    var postId: String? = null,
    var username: String? = null,
    var storageFileName: String? = null,
    var userId: String? = null,
    var caption: String? = null,
    var likes: Int? = 0,
    var uploadedAt: Timestamp? = null,
    var likedBy: @RawValue List<DocumentReference>? = ArrayList()
) : Parcelable