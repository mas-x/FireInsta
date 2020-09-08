package com.masrooraijaz.fireinsta.repository.main

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.masrooraijaz.fireinsta.util.FirestoreConstants
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.COMMENT_ORDER_BY_FIELD_NAME
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.COMMENT_WHERE_EQUAL_TO_COLUMN_NAME
import com.masrooraijaz.fireinsta.util.FirestoreConstants.Companion.POSTS_ORDER_BY_FIELD_NAME
import org.koin.core.KoinComponent
import org.koin.core.inject

class HomeRepository : KoinComponent {

    private val db by inject<FirebaseFirestore>()
    private val storage by inject<FirebaseStorage>()
    fun getPostsQuery(): Query {
        return db.collection(FirestoreConstants.POSTS_COLLECTION)
            .orderBy(POSTS_ORDER_BY_FIELD_NAME, Query.Direction.DESCENDING)
    }

    fun getPostCommentsQuery(postId: String): Query {
        return db.collection(FirestoreConstants.POST_COMMENTS_COLLECTION)
            .whereEqualTo(COMMENT_WHERE_EQUAL_TO_COLUMN_NAME, postId)
            .orderBy(COMMENT_ORDER_BY_FIELD_NAME, Query.Direction.ASCENDING)
    }

    fun getPostsStorage(): StorageReference {
        return storage.reference
    }

}