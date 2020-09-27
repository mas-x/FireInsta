package com.masrooraijaz.fireinsta.util

class FirestoreConstants {
    companion object {


        const val POSTS_LIKED_BY_FIELD = "likedBy"

        const val USERS_COLLECTION = "users"

        const val POSTS_COLLECTION = "posts"


        const val POST_COMMENTS_COLLECTION = "post_comments"
        const val COMMENT_WHERE_EQUAL_TO_COLUMN_NAME = "postId"


        const val USER_WHERE_EQUAL_TO_FIELD_NAME = "displayName"

        const val SEARCH_PHOTO_WHERE_EQUAL_TO_FIELD_NAME = "caption"

        const val POSTS_ORDER_BY_FIELD_NAME = "uploadedAt"
        const val COMMENT_ORDER_BY_FIELD_NAME = "time"

        const val USER_FOLLOWERS_FIELD = "followersRef"
        const val USER_FOLLOWING_FIELD = "followingRef"

        const val USER_POSTS_COUNT_FILED = "posts"

        const val POST_USER_ID_FIELD = "userId"
    }
}