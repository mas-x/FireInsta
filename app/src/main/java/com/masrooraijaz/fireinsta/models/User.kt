package com.masrooraijaz.fireinsta.models

import android.os.Parcelable
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class User(
    @Exclude
    var id: String? = null,
    val displayName: String = "",
    val posts: Int = -1,
    val followers: Int = -1,
    val following: Int = -1,
    var followersRef: @RawValue List<DocumentReference>? = null,
    var followingRef: @RawValue List<DocumentReference>? = null
) : Parcelable