package com.masrooraijaz.fireinsta.ui.main.upload

import android.net.Uri
import androidx.lifecycle.*
import com.masrooraijaz.fireinsta.repository.main.UploadRepository
import com.masrooraijaz.fireinsta.util.DataOrException
import com.masrooraijaz.fireinsta.util.Message
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.lang.Exception

class UploadViewModel : ViewModel(), KoinComponent {

    private val repository by inject<UploadRepository>()

    val selectedImageUri = MutableLiveData<Uri>()
    fun uploadFile(imageCaption: String, uri: Uri): LiveData<DataOrException<Message, Exception>> {
        val mediatorLiveData = MediatorLiveData<DataOrException<Message, Exception>>()

        val result = repository.uploadPicture(imageCaption, uri)
        mediatorLiveData.value = DataOrException(loading = true)

        mediatorLiveData.addSource(result, Observer {
            mediatorLiveData.value = it
        })

        return mediatorLiveData
    }
}