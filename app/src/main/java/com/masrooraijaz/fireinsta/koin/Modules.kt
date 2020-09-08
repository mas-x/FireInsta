package com.masrooraijaz.fireinsta.koin

import androidx.paging.PagedList
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.repository.auth.AuthRepository
import com.masrooraijaz.fireinsta.repository.main.AccountRepository
import com.masrooraijaz.fireinsta.repository.main.HomeRepository
import com.masrooraijaz.fireinsta.repository.main.UploadRepository
import com.masrooraijaz.fireinsta.ui.auth.AuthViewModel
import com.masrooraijaz.fireinsta.ui.main.account.AccountViewModel
import com.masrooraijaz.fireinsta.ui.main.home.HomeViewModel
import com.masrooraijaz.fireinsta.ui.main.upload.UploadViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module


val appModule = module {
    single { AuthRepository() }
    single { AccountRepository() }
    single {
        RequestOptions().placeholder(R.drawable.default_image).error(R.drawable.default_image)
    }
    single { Glide.with(androidContext()).setDefaultRequestOptions(get()) }
    single { UploadRepository() }
    single { HomeRepository() }

}
val viewModelsModule = module {
    viewModel { AuthViewModel() }
    viewModel { AccountViewModel() }
    viewModel { UploadViewModel() }
    viewModel { HomeViewModel() }
}


val firebaseModule: Module = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { Firebase.storage }
}

val allModules = listOf(firebaseModule, appModule, viewModelsModule)
