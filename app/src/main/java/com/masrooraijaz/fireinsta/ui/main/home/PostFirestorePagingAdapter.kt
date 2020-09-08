package com.masrooraijaz.fireinsta.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_post.view.*
import kotlinx.android.synthetic.main.layout_profile_image.view.*

class PostFirestorePagingAdapter(
    options: FirestorePagingOptions<Post?>,
    val statesListener: StatesListener,
    val requestManager: RequestManager,
    val storageReference: StorageReference,
    val interactionListener: InteractionListener<Post>
) :
    FirestorePagingAdapter<Post, PostViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post, parent, false),
            storageReference,
            requestManager,
            interactionListener
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int, post: Post) {
        post.postId = this.getItem(position)?.id
        holder.bindPost(post)
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        when (state) {
            LoadingState.LOADING_INITIAL -> {
                statesListener.onLoad()
            }
            LoadingState.LOADING_MORE -> {
                statesListener.onLoadingMore()
            }
            LoadingState.LOADED -> {
                statesListener.onLoaded()
            }

            LoadingState.FINISHED -> {
                statesListener.onFinished()
            }
            LoadingState.ERROR -> {
                statesListener.onLoaded()
            }
            else -> {
                //do nothing...
            }
        }
    }

}


class PostViewHolder(
    itemView: View,
    val storageReference: StorageReference,
    val requestManager: RequestManager,
    val interactionListener: InteractionListener<Post>
) : RecyclerView.ViewHolder(itemView) {


    fun bindPost(post: Post) {
        requestManager.load(
            post.userId?.let { userId ->
                post.storageFileName?.let {
                    storageReference.child(userId)
                        .child(FirebaseStoragePaths.USER_IMAGES_PATH).child(it)
                }
            }

        ).into(itemView.image_post)

        itemView.text_username.text = post.username
        itemView.text_favt_count.text = post.likes.toString()

        itemView.setOnClickListener {
            interactionListener.onClick(post)
        }
    }
}