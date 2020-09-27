package com.masrooraijaz.fireinsta.ui.main.home

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
import com.masrooraijaz.fireinsta.ui.FragmentItemsListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_post.view.*

class PostFirestoreRecyclerAdapter(
    options: FirestoreRecyclerOptions<Post?>,
    statesListener: StatesListener,
    requestManager: RequestManager,
    storageReference: StorageReference,
    interactionListener: PostInteractionListener,
    val fragmentItemsListener: FragmentItemsListener,
    userReference: DocumentReference
) :
    BasePostsFirestorRecyclerAdapter<PostViewHolder, PostInteractionListener>(
        options,
        statesListener,
        requestManager,
        storageReference,
        interactionListener,
        userReference
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_post, parent, false),
            storageReference,
            requestManager,
            interactionListener,
            userReference
        )
    }

    override fun bindPost(holder: PostViewHolder, position: Int, post: Post) {
        post.postId = snapshots.getSnapshot(position).id
        holder.bindPost(post)
    }

    override fun onDataChanged() {
        super.onDataChanged()

        fragmentItemsListener.onDataChanged(itemCount)
    }

}


class PostViewHolder(
    itemView: View,
    val storageReference: StorageReference,
    val requestManager: RequestManager,
    val interactionListener: PostInteractionListener,
    val userReference: DocumentReference
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

        //load display pic
        requestManager.load(
            post.userId?.let {
                storageReference.child(FirebaseStoragePaths.DISPLAY_PIC_PATH).child(
                    it
                )
            }
        ).error(R.drawable.ic_baseline_account_circle_24)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(itemView.image_user_dp)

        itemView.text_username.text = post.username
        itemView.text_favt_count.text = post.likes.toString()

        itemView.image_post.setOnClickListener {
            interactionListener.onClick(post)
        }

        itemView.image_favt.setOnClickListener {
            interactionListener.onFavt(post)
        }

        itemView.image_comments.setOnClickListener {
            interactionListener.onComment(post)
        }

        itemView.text_favt_count.text = post.likedBy?.count().toString()
        post.likedBy?.let {

            if (it.contains(userReference)) {
                itemView.image_favt.setImageResource(R.drawable.ic_filled_favorite_24)
            }
        }
    }
}


interface PostInteractionListener {
    fun onClick(item: Post)
    fun onFavt(item: Post)
    fun onComment(item: Post)
}
