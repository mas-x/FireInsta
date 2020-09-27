package com.masrooraijaz.fireinsta.ui.main.home

import android.text.format.DateUtils
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
import com.masrooraijaz.fireinsta.models.Comment
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths
import kotlinx.android.synthetic.main.layout_comment.view.*
import java.util.*


class CommentsFirestorePagingAdapter(
    options: FirestorePagingOptions<Comment?>,
    val statesListener: StatesListener,
    val interactionListener: InteractionListener<Comment>,
    val storageReference: StorageReference,
    val requestManager: RequestManager
) :
    FirestorePagingAdapter<Comment, CommentViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_comment, parent, false),
            interactionListener, storageReference, requestManager
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int, comment: Comment) {
        holder.bindPost(comment)
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


class CommentViewHolder(
    itemView: View,
    val interactionListener: InteractionListener<Comment>,
    val storageReference: StorageReference,
    val requestManager: RequestManager
) : RecyclerView.ViewHolder(itemView) {


    fun bindPost(comment: Comment) {

        //load display pic
        requestManager.load(
            comment.userId?.let {
                storageReference.child(FirebaseStoragePaths.DISPLAY_PIC_PATH).child(
                    it
                )
            }
        ).error(R.drawable.ic_baseline_account_circle_24)
            .placeholder(R.drawable.ic_baseline_account_circle_24)
            .into(itemView.image_comment_dp)

        itemView.text_username.text = comment.username
        itemView.text_comment.text = comment.comment
        itemView.text_comment_time.text = comment.time?.let {
            DateUtils.formatSameDayTime(
                it.toDate().time,
                Date().time,
                java.text.DateFormat.SHORT,
                java.text.DateFormat.SHORT
            )
        }

    }
}