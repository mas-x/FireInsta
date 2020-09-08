package com.masrooraijaz.fireinsta.util

import androidx.paging.PagedList
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreUtil {


    companion object {



        fun getPagedListConfig(pageSize: Int, prefetchDistance: Int): PagedList.Config {
            return PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(prefetchDistance)
                .setPageSize(pageSize)
                .build()
        }

    }
}