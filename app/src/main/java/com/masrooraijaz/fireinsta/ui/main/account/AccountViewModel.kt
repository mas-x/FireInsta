package com.masrooraijaz.fireinsta.ui.main.account

import android.net.Uri
import androidx.lifecycle.*
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.repository.main.AccountRepository
import com.masrooraijaz.fireinsta.util.DataOrException
import com.masrooraijaz.fireinsta.util.Message
import org.koin.core.KoinComponent
import org.koin.core.inject

class AccountViewModel : ViewModel(), KoinComponent {

    private val repository by inject<AccountRepository>()

    val currentAccountId = MutableLiveData<String>()

    val selectedImageUri = MutableLiveData<Uri>()

    val currentAccount = MediatorLiveData<DataOrException<User, Exception>>()

    fun setCurrentAccountId(accountId: String) {
        currentAccountId.value = accountId
    }

    fun setCurrentUserAccountId() {
        currentAccountId.value = repository.getUserAccountId()
    }

    fun getAccount(accountId: String): LiveData<DataOrException<User, Exception>> {

        currentAccount.value = DataOrException(loading = true)

        currentAccount.addSource(repository.getAccount(accountId), Observer {
            currentAccount.value = it
        })

        return currentAccount
    }

    fun getUserImagesQuery(userId: String) = repository.getUserImagesQuery(userId)
    fun getStorageReference(userId: String) = repository.getUserStorageReference(userId)

    fun followUser() = repository.followUser(currentAccountId.value!!)
    fun unFollowUser() = repository.unFollowUser(currentAccountId.value!!)

    fun getLoggedInUserId() = repository.getUserAccountId()

    fun getUserRef(accountId: String) = repository.getUserRef(accountId)


    fun updateProfilePicture(uri: Uri): LiveData<DataOrException<Message, Exception>> {

        val res = MediatorLiveData<DataOrException<Message, Exception>>()

        res.value = DataOrException(
            loading = true
        )

        val source = repository.updateProfilePicture(uri)

        res.addSource(source, Observer {
            res.value = it
            res.removeSource(source)
        })

        return res
    }

    fun getUserProfilePicStorageRef() =
        currentAccountId.value?.let { repository.getUserProfilePicStorageRef(it) }

    fun logOut() = repository.logOut()

}