package com.masrooraijaz.fireinsta.ui.main.account

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_profile_image.view.*
import java.lang.Exception

private const val TAG = "ProfileFirestorePagingA"

class ProfileFirestorePagingAdapter(
    options: FirestorePagingOptions<Post?>,
    val storageReference: StorageReference,
    val requestManager: RequestManager
) :
    FirestorePagingAdapter<Post, ProfileImageViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileImageViewHolder {
        return ProfileImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_profile_image,
                parent,
                false
            ), storageReference, requestManager
        )
    }

    override fun onBindViewHolder(holder: ProfileImageViewHolder, position: Int, post: Post) {
        holder.bind(post)
    }

    override fun onError(e: Exception) {
        super.onError(e)
        Log.e(TAG, "onError: ${e.message}")
    }
}

class ProfileImageViewHolder(
    itemView: View,
    val storageReference: StorageReference,
    val requestManager: RequestManager
) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(post: Post) {
        requestManager.load(
            post.storageFileName?.let {
                storageReference.child(FirebaseStoragePaths.USER_IMAGES_PATH)
                    .child(it)
            }
        ).into(itemView.image_view)
    }
}