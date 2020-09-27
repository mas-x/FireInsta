package com.masrooraijaz.fireinsta.repository.main

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.models.Comment
import com.masrooraijaz.fireinsta.models.Post
import com.masrooraijaz.fireinsta.util.*
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.POSTS_ORDER_BY_FIELD_NAME
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.SEARCH_PHOTO_WHERE_EQUAL_TO_FIELD_NAME
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.USER_WHERE_EQUAL_TO_FIELD_NAME
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

private const val TAG = "HomeRepository"

class HomeRepository : KoinComponent {

    private val db by inject<FirebaseFirestore>()

    fun getUserAccountQuery(userAccount: String): Query {
        return db.collection(FirestoreConstants.USERS_COLLECTION)
            .orderBy(USER_WHERE_EQUAL_TO_FIELD_NAME)
            .startAt(userAccount)
            .endAt(userAccount + "\uf8ff")
    }


    fun getPostsQuery(): Query {

        //todo only show posts of user which the user is following.
        return db.collection(FirestoreConstants.POSTS_COLLECTION)
            .orderBy(POSTS_ORDER_BY_FIELD_NAME, Query.Direction.DESCENDING)
    }


    fun getSearchPostsQuery(searchText: String): Query {
        return db.collection(FirestoreConstants.POSTS_COLLECTION)
            .orderBy(SEARCH_PHOTO_WHERE_EQUAL_TO_FIELD_NAME)
            .startAt(searchText)
            .endAt(searchText + "\uf8ff")
    }

    private val auth by inject<FirebaseAuth>()

    private val storage by inject<FirebaseStorage>()

    fun postComment(
        postId: String,
        commentStr: String
    ): LiveData<DataOrException<Message, Exception>> {

        val mutableLiveData = MutableLiveData<DataOrException<Message, Exception>>()

        val comment = Comment(
            commentStr,
            postId,
            Timestamp.now(),
            auth.uid,
            auth.currentUser?.displayName //i.e. Masroor Aijaz
        )
        db.collection(FirestoreConstants.POST_COMMENTS_COLLECTION).add(
            comment
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                mutableLiveData.value = DataOrException<Message, Exception>(
                    data = Message(
                        "Comment posted"
                    )
                )
            } else {
                mutableLiveData.value = DataOrException(
                    exception = it.exception
                )
            }
        }
        return mutableLiveData
    }


    fun favtPost(postId: String): LiveData<DataOrException<Message, Exception>> {

        val mutableLiveData = MutableLiveData<DataOrException<Message, Exception>>()
        val docReference = getUserDocumentReference()

        db.collection(FirestoreConstants.POSTS_COLLECTION).document(postId).update(
            FirestoreConstants.POSTS_LIKED_BY_FIELD, FieldValue.arrayUnion(docReference)
        ).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                Log.d(TAG, "favtPost: Task successfull...")
                mutableLiveData.value = DataOrException(
                    data = Message(
                        SuccessHandling.FAVT_SUCCESS
                    )
                )
            } else {
                mutableLiveData.value = DataOrException(
                    exception = it.exception
                )
            }
        })

        return mutableLiveData
    }

    fun unFavtPost(postId: String): LiveData<DataOrException<Message, Exception>> {
        val mutableLiveData = MutableLiveData<DataOrException<Message, Exception>>()
        val docReference = db.collection(FirestoreConstants.USERS_COLLECTION).document(auth.uid!!)

        db.collection(FirestoreConstants.POSTS_COLLECTION).document(postId).update(
            FirestoreConstants.POSTS_LIKED_BY_FIELD, FieldValue.arrayRemove(docReference)
        ).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                mutableLiveData.value = DataOrException(
                    data = Message(
                        SuccessHandling.UNFAVT_SUCCESS
                    )
                )
            } else {
                mutableLiveData.value = DataOrException(
                    exception = it.exception
                )
            }
        })

        return mutableLiveData
    }


    fun getUserDocumentReference(): DocumentReference {

        return db.collection(FirestoreConstants.USERS_COLLECTION).document(auth.uid!!)
    }

    fun getPostsStorage(): StorageReference {
        return storage.reference
    }


    fun getPostCommentsQuery(postId: String): Query {
        return db.collection(FirestoreConstants.POST_COMMENTS_COLLECTION)
            .whereEqualTo(FirestoreConstants.COMMENT_WHERE_EQUAL_TO_COLUMN_NAME, postId)
            .orderBy(FirestoreConstants.COMMENT_ORDER_BY_FIELD_NAME, Query.Direction.ASCENDING)
    }

    fun uploadPicture(
        imageCaption: String,
        fileUri: Uri
    ): LiveData<DataOrException<Message, Exception>> {

        val result = MutableLiveData<DataOrException<Message, Exception>>()

        auth.uid?.let { uid ->
            val uuid = UUID.randomUUID().toString()
            storage.reference.child(uid).child(FirebaseStoragePaths.USER_IMAGES_PATH)
                .child(uuid)
                .putFile(fileUri).addOnCompleteListener(OnCompleteListener { uploadTask ->
                    if (uploadTask.isSuccessful) {
                        val post = Post()
                        post.caption = imageCaption
                        post.userId = uid
                        post.storageFileName = uuid
                        post.uploadedAt = Timestamp.now()
                        post.username = auth.currentUser?.displayName
                        db.collection(FirestoreConstants.POSTS_COLLECTION)
                            .add(post).addOnCompleteListener { postTask ->
                                if (postTask.isSuccessful) {

                                    db.collection(FirestoreConstants.USERS_COLLECTION)
                                        .document(auth.uid!!).update(
                                            FirestoreConstants.USER_POSTS_COUNT_FILED,
                                            FieldValue.increment(1)
                                        ).addOnCompleteListener { postCountIncrementTask ->

                                            if (postCountIncrementTask.isSuccessful) {
                                                result.value = DataOrException(
                                                    data = Message(
                                                        SuccessHandling.FILE_UPLOAD_SUCCESS
                                                    )
                                                )
                                            } else {
                                                result.value = DataOrException(
                                                    exception = postCountIncrementTask.exception
                                                )
                                            }

                                        }


                                } else {
                                    result.value = DataOrException(
                                        exception = postTask.exception
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

    fun deletePost(postId: String): LiveData<DataOrException<Message, Exception>> {

        val mutableLiveData = MutableLiveData<DataOrException<Message, Exception>>()

        db.collection(FirestoreConstants.POSTS_COLLECTION).document(postId).delete()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    auth.uid?.let {
                        db.collection(FirestoreConstants.USERS_COLLECTION).document(it).update(
                            FirestoreConstants.USER_POSTS_COUNT_FILED, FieldValue.increment(-1)
                        )
                    }
                    mutableLiveData.value = DataOrException(
                        data = Message(
                            SuccessHandling.DELETE_PIC_SUCCESS
                        )
                    )
                } else {
                    mutableLiveData.value = DataOrException(
                        exception = task.exception
                    )
                }
            }
        return mutableLiveData
    }

    fun getLoggedInUid(): String? {
        return auth.uid
    }


}