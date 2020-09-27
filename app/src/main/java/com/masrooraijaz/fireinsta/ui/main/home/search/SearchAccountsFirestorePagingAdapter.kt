package com.masrooraijaz.fireinsta.ui.main.home.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.ui.InteractionListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import kotlinx.android.synthetic.main.layout_account_item.view.*

class SearchAccountsFirestorePagingAdapter(
    options: FirestorePagingOptions<User>,
    statesListener: StatesListener,
    requestManager: RequestManager,
    storageReference: StorageReference,
    val interactionListener: InteractionListener<User>
) : FirestorePagingAdapter<User, UserViewHolder>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_account_item, parent, false
            ), interactionListener
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
        model.id = getItem(position)?.id
        holder.bindUser(model)
    }


}

class UserViewHolder(itemView: View, val listener: InteractionListener<User>) :
    RecyclerView.ViewHolder(itemView) {

    fun bindUser(user: User) {

        itemView.text_username.text = user.displayName

        itemView.text_user_photos_count.text = user.posts.toString()

        itemView.setOnClickListener {
            listener.onClick(user)
        }
    }
}