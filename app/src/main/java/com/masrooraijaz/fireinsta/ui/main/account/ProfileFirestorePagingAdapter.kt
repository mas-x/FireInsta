package com.masrooraijaz.fireinsta.ui.main.account

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.FragmentItemsListener
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_profile_image.view.*

private const val TAG = "ProfileFirestorePagingA"

class ProfileFirestorePagingAdapter(
    options: FirestorePagingOptions<Post?>,
    val storageReference: StorageReference,
    val requestManager: RequestManager,
    val fragmentItemsListener: FragmentItemsListener?,
    val interactionListener: InteractionListener<Post>
) :
    FirestorePagingAdapter<Post, ProfileImageViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileImageViewHolder {
        return ProfileImageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_profile_image,
                parent,
                false
            ), storageReference, requestManager, interactionListener
        )
    }

    override fun onBindViewHolder(holder: ProfileImageViewHolder, position: Int, post: Post) {
        post.postId = getItem(position)?.id
        holder.bind(post)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        super.onLoadingStateChanged(state)

        when (state) {
            LoadingState.LOADING_INITIAL -> {

            }
            LoadingState.LOADING_MORE -> {

            }
            LoadingState.LOADED -> {

            }

            LoadingState.FINISHED -> {
                fragmentItemsListener?.onDataChanged(itemCount)
            }
            LoadingState.ERROR -> {

            }
            else -> {
                //do nothing...
            }
        }
    }


    override fun onError(e: Exception) {
        super.onError(e)
        Log.e(TAG, "onError: ${e.message}")
    }
}

class ProfileImageViewHolder(
    itemView: View,
    val storageReference: StorageReference,
    val requestManager: RequestManager,
    val interactionListener: InteractionListener<Post>
) :
    RecyclerView.ViewHolder(itemView) {

    fun bind(post: Post) {
        requestManager.load(
            post.storageFileName?.let {
                storageReference.child(FirebaseStoragePaths.USER_IMAGES_PATH)
                    .child(it)
            }
        )
            .into(itemView.image_view)

        itemView.image_view.setOnClickListener {
            interactionListener.onClick(post)
        }

    }
}