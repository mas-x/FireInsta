package com.masrooraijaz.fireinsta.repository.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.util.DataOrException
import com.masrooraijaz.fireinsta.util.FirebaseStoragePaths.Companion.DISPLAY_PIC_PATH
import com.masrooraijaz.fireinsta.util.FirestoreConstants
import com.masrooraijaz.fireinsta.util.Message
import com.masrooraijaz.fireinsta.util.SuccessHandling
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
        return db.collection(FirestoreConstants.POSTS_COLLECTION)
            .whereEqualTo(FirestoreConstants.POST_USER_ID_FIELD, userId)
            .orderBy(FirestoreConstants.POSTS_ORDER_BY_FIELD_NAME, Query.Direction.DESCENDING)
    }

    fun getUserStorageReference(userId: String): StorageReference {
        return storageReference.reference.child(userId)

    }

    fun getUserAccountId() = auth.uid

    fun followUser(accountId: String): LiveData<DataOrException<Message, Exception>> {

        val res = MutableLiveData<DataOrException<Message, Exception>>()

        auth.uid?.let { uid ->
            val followUserRef = db.collection(FirestoreConstants.USERS_COLLECTION)
                .document(accountId)

            //add to current user's following field.
            db.collection(FirestoreConstants.USERS_COLLECTION).document(uid).update(
                FirestoreConstants.USER_FOLLOWING_FIELD, FieldValue.arrayUnion(followUserRef)
            )


            val currentUserRef = db.collection(FirestoreConstants.USERS_COLLECTION).document(uid)
            db.collection(FirestoreConstants.USERS_COLLECTION).document(accountId).update(
                FirestoreConstants.USER_FOLLOWERS_FIELD, FieldValue.arrayUnion(currentUserRef)
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    res.value = DataOrException(
                        data = Message(
                            SuccessHandling.FOLLOW_SUCCESS
                        )
                    )
                } else {
                    res.value = DataOrException(
                        exception = it.exception
                    )
                }
            }
        }
        return res
    }


    fun unFollowUser(accountId: String): LiveData<DataOrException<Message, Exception>> {

        val res = MutableLiveData<DataOrException<Message, Exception>>()

        auth.uid?.let { uid ->
            val followUserRef = db.collection(FirestoreConstants.USERS_COLLECTION)
                .document(accountId)

            //add to current user's following field.
            db.collection(FirestoreConstants.USERS_COLLECTION).document(uid).update(
                FirestoreConstants.USER_FOLLOWING_FIELD, FieldValue.arrayRemove(followUserRef)
            )


            val currentUserRef = db.collection(FirestoreConstants.USERS_COLLECTION).document(uid)
            db.collection(FirestoreConstants.USERS_COLLECTION).document(accountId).update(
                FirestoreConstants.USER_FOLLOWERS_FIELD, FieldValue.arrayRemove(currentUserRef)
            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    res.value = DataOrException(
                        data = Message(
                            SuccessHandling.UNFOLLOW_SUCCESS
                        )
                    )
                } else {
                    res.value = DataOrException(
                        exception = it.exception
                    )
                }
            }
        }

        return res
    }


    fun getUserRef(accountId: String) =
        db.collection(FirestoreConstants.USERS_COLLECTION).document(accountId)

    fun updateProfilePicture(uri: Uri): LiveData<DataOrException<Message, Exception>> {

        val result = MutableLiveData<DataOrException<Message, Exception>>()

        auth.uid?.let {
            storageReference.getReference(DISPLAY_PIC_PATH).child(it).putFile(uri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.value = DataOrException(
                            data = Message(
                                SuccessHandling.DISPLAY_PIC_UPDATED
                            )
                        )
                    } else {
                        result.value = DataOrException(
                            exception = task.exception
                        )
                    }
                }
        }

        return result
    }

    fun getUserProfilePicStorageRef(accountId: String): StorageReference {
        return storageReference.getReference(DISPLAY_PIC_PATH).child(accountId)
    }

    fun logOut() {
        auth.signOut()
    }

}