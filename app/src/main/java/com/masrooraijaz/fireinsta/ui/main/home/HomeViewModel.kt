package com.masrooraijaz.fireinsta.ui.main.home

import android.net.Uri
import androidx.lifecycle.*
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.repository.main.HomeRepository
import com.masrooraijaz.fireinsta.util.DataOrException
import com.masrooraijaz.fireinsta.util.Message
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeViewModel : ViewModel(), KoinComponent {


    var searchAccountQuery = MutableLiveData<String>()

    fun setSearchAccount(account: String) {
        searchAccountQuery.value = account
    }

    val homeRepository by inject<HomeRepository>()


    fun getPostsQuery() = homeRepository.getPostsQuery()


    fun getSearchUserAccountsQuery(username: String) = homeRepository.getUserAccountQuery(username)

    fun getSearchPostsQuery(searchText: String) = homeRepository.getSearchPostsQuery(searchText)


    val selectedPost = MutableLiveData<Post>()

    fun setSelectedPost(post: Post) {
        selectedPost.value = post
    }

    fun postComment(
        commentString: String
    ): LiveData<DataOrException<Message, Exception>> {

        val mediatorLiveData = MediatorLiveData<DataOrException<Message, Exception>>()
        mediatorLiveData.value = DataOrException(loading = true)
        val source = homeRepository.postComment(
            selectedPost.value?.postId!!, commentString
        )
        mediatorLiveData.addSource(source, Observer {
            mediatorLiveData.value = it
            mediatorLiveData.removeSource(mediatorLiveData)
        })


        return mediatorLiveData

    }

    fun getUserDocumentReference() = homeRepository.getUserDocumentReference()
    fun getStorageReference() = homeRepository.getPostsStorage()


    fun favtPost(): LiveData<DataOrException<Message, Exception>>? {

        selectedPost.value?.let { post ->

            post.likedBy?.let { likedBy ->
                val likedByMutableList = likedBy.toMutableList()
                val userReference = homeRepository.getUserDocumentReference()

                return if (likedByMutableList.contains(userReference)) {
                    likedByMutableList.remove(userReference)
                    post.likedBy = likedByMutableList.toList()
                    setSelectedPost(post)
                    homeRepository.unFavtPost(selectedPost.value?.postId!!)

                } else {
                    likedByMutableList.add(userReference)
                    post.likedBy = likedByMutableList.toList()
                    setSelectedPost(post)
                    likedByMutableList.add(userReference)
                    homeRepository.favtPost(post.postId!!)
                }
            }

        }

        return null
    }

    fun getPostCommentsQuery(postId: String) = homeRepository.getPostCommentsQuery(postId)


    val selectedImageUri = MutableLiveData<Uri>()
    fun uploadFile(imageCaption: String, uri: Uri): LiveData<DataOrException<Message, Exception>> {
        val mediatorLiveData = MediatorLiveData<DataOrException<Message, Exception>>()

        val result = homeRepository.uploadPicture(imageCaption, uri)
        mediatorLiveData.value = DataOrException(loading = true)

        mediatorLiveData.addSource(result, Observer {
            mediatorLiveData.value = it
        })

        return mediatorLiveData
    }

    fun deletePost(): MediatorLiveData<DataOrException<Message, Exception>> {
        val mediatorLiveData = MediatorLiveData<DataOrException<Message, Exception>>()

        mediatorLiveData.value = DataOrException(loading = true)

        val result = homeRepository.deletePost(selectedPost.value?.postId!!)

        mediatorLiveData.addSource(result, Observer {
            mediatorLiveData.value = it
            mediatorLiveData.removeSource(result)
        })

        return mediatorLiveData
    }

    fun getLoggedInUserUid(): String? = homeRepository.getLoggedInUid()


}