package com.masrooraijaz.fireinsta.util

class FirestoreConstants {
    companion object {
        const val POSTS_COLLECTION = "posts"


        const val POST_COMMENTS_COLLECTION = "post_comments"
        const val COMMENT_WHERE_EQUAL_TO_COLUMN_NAME = "postId"


        const val POSTS_ORDER_BY_FIELD_NAME = "uploadedAt"
        const val COMMENT_ORDER_BY_FIELD_NAME = "time"
    }
}