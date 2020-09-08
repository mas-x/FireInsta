package com.masrooraijaz.fireinsta.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.repository.main.HomeRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {


    val selectedPost = MutableLiveData<Post>()


    fun setSelectedPost(post: Post) {
        selectedPost.value = post
    }

    val postsRepository by inject<HomeRepository>()
    fun getPostsQuery() = postsRepository.getPostsQuery()

    fun getPostCommentsQuery(postId: String) = postsRepository.getPostCommentsQuery(postId)

    fun getStorageReference() = postsRepository.getPostsStorage()
}