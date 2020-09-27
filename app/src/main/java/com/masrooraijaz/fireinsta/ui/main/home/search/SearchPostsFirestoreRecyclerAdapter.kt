package com.masrooraijaz.fireinsta.ui.main.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import com.masrooraijaz.fireinsta.ui.main.home.BasePostsFirestorRecyclerAdapter
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_post.view.*

class SearchPostsFirestoreRecyclerAdapter(
    options: FirestoreRecyclerOptions<Post?>,
    statesListener: StatesListener,
    requestManager: RequestManager,
    storageReference: StorageReference,
    interactionListener: InteractionListener<Post>,
    userReference: DocumentReference
) : BasePostsFirestorRecyclerAdapter<PictureViewHolder, InteractionListener<Post>>(
    options, statesListener, requestManager, storageReference, interactionListener, userReference
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_picture, parent, false),
            storageReference,
            requestManager,
            interactionListener
        )
    }

    override fun bindPost(holder: PictureViewHolder, position: Int, post: Post) {
        holder.bindPost(post)
    }
}


class PictureViewHolder(
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

        ).placeholder(R.drawable.black_default_image)
            .into(itemView.image_post)

        itemView.setOnClickListener {
            interactionListener.onClick(post)
        }
    }
}
