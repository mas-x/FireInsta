package com.masrooraijaz.fireinsta.ui.main.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.repository.main.AccountRepository
import com.masrooraijaz.fireinsta.util.DataOrException
import org.koin.core.KoinComponent
import org.koin.core.inject

class AccountViewModel : ViewModel(), KoinComponent {

    private val repository by inject<AccountRepository>()
    fun getAccount(accountId: String): LiveData<DataOrException<User, Exception>> {
        val result = MediatorLiveData<DataOrException<User, Exception>>()
        result.value = DataOrException(loading = true)

        result.addSource(repository.getAccount(accountId), Observer {
            result.value = it
        })

        return result
    }

    fun getUserImagesQuery(userId: String) = repository.getUserImagesQuery(userId)
    fun getStorageReference(userId: String) = repository.getUserStorageReference(userId)

}