package com.masrooraijaz.fireinsta.repository.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.util.DataOrException
import org.koin.core.KoinComponent
import org.koin.core.inject

class AccountRepository : KoinComponent {
    private val db by inject<FirebaseFirestore>()
    private val auth by inject<FirebaseAuth>()
    private val storageReference by inject<FirebaseStorage>()
    fun getAccount(userId: String): LiveData<DataOrException<User, Exception>> {

        val result: MutableLiveData<DataOrException<User, Exception>> =
            MutableLiveData()

        db.collection("users").document(userId).get().addOnCompleteListener {
            if (it.isSuccessful) {
                //do success
                result.value = DataOrException(
                    data = it.result?.toObject(User::class.java)
                )
            } else {
                result.value = DataOrException(
                    exception = it.exception
                )
            }
        }
        return result
    }

    fun getUserImagesQuery(userId: String): Query {
        return db.collection("posts").whereEqualTo("userId", userId)
    }

    fun getUserStorageReference(userId: String): StorageReference {
        return storageReference.reference.child(userId)

    }
}