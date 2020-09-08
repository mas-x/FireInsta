package com.masrooraijaz.fireinsta.repository.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.util.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

class UploadRepository : KoinComponent {

    private val storageReference by inject<FirebaseStorage>()
    private val firestore by inject<FirebaseFirestore>()

    private val auth by inject<FirebaseAuth>()

    fun uploadPicture(
        imageCaption: String,
        fileUri: Uri
    ): LiveData<DataOrException<Message, Exception>> {

        val result = MutableLiveData<DataOrException<Message, Exception>>()

        auth.uid?.let { uid ->
            val uuid = UUID.randomUUID().toString()
            storageReference.reference.child(uid).child(FirebaseStoragePaths.USER_IMAGES_PATH)
                .child(uuid)
                .putFile(fileUri).addOnCompleteListener(OnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        val post = Post()
                        post.caption = imageCaption
                        post.userId = uid
                        post.storageFileName = uuid
                        post.uploadedAt = Timestamp.now()
                        post.username = auth.currentUser?.displayName
                        firestore.collection(FirestoreConstants.POSTS_COLLECTION)
                            .add(post).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    result.value = DataOrException<Message, Exception>(
                                        data = Message(
                                            SuccessHandling.FILE_UPLOAD_SUCCESS
                                        )
                                    )
                                } else {
                                    result.value = DataOrException(
                                        exception = uploadTask.exception
                                    )
                                }
                            }
                    } else {
                        result.value = DataOrException(
                            exception = uploadTask.exception
                        )
                    }
                })
        }
        return result
    }


}