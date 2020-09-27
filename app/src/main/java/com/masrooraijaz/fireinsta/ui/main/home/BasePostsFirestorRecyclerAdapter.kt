package com.masrooraijaz.fireinsta.ui.main.home

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.ui.main.StatesListener

abstract class BasePostsFirestorRecyclerAdapter<T : RecyclerView.ViewHolder?, I>(
    options: FirestoreRecyclerOptions<Post?>,
    val statesListener: StatesListener,
    val requestManager: RequestManager,
    val storageReference: StorageReference,
    val interactionListener: I,
    val userReference: DocumentReference
) : FirestoreRecyclerAdapter<Post, T>(options) {

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): T

    override fun onBindViewHolder(holder: T, position: Int, model: Post) {
        model.postId = snapshots.getSnapshot(position).id
        bindPost(holder, position, model);
    }

    abstract fun bindPost(holder: T, position: Int, post: Post)

    override fun onDataChanged() {
        statesListener.onFinished()
    }

}