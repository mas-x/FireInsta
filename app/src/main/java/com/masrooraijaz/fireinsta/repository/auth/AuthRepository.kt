package com.masrooraijaz.fireinsta.repository.auth

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.masrooraijaz.fireinsta.models.User
import com.masrooraijaz.fireinsta.util.DataOrException
import com.masrooraijaz.fireinsta.util.FirestoreConstants
import org.koin.core.KoinComponent
import org.koin.core.inject

private const val TAG = "AuthRepository"

class AuthRepository : KoinComponent {

    private val auth by inject<FirebaseAuth>()
    private val db by inject<FirebaseFirestore>()


    fun createUser(
        displayName: String,
        email: String,
        password: String
    ): MutableLiveData<DataOrException<FirebaseUser, Exception>> {
        val mutableLiveData = MutableLiveData<DataOrException<FirebaseUser, Exception>>()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName).build()
                task.result?.user?.updateProfile(profileUpdate)
                    ?.addOnCompleteListener { profileUpdateTask ->
                        if (profileUpdateTask.isSuccessful) {
                            task.result?.user?.let { firebaseUser ->

                                //add to database
                                val user = User(
                                    null,
                                    displayName,
                                    0,
                                    0,
                                    0,
                                    null,
                                    null
                                )
                                db.collection(FirestoreConstants.USERS_COLLECTION).document(
                                    firebaseUser.uid
                                ).set(user).addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        mutableLiveData.value = DataOrException(
                                            data = firebaseUser,
                                            exception = null
                                        )
                                    } else {
                                        mutableLiveData.value = DataOrException(
                                            data = null,
                                            exception = profileUpdateTask.exception
                                        )
                                    }
                                }

                            }
                        } else {
                            mutableLiveData.value = DataOrException(
                                data = null,
                                exception = profileUpdateTask.exception
                            )
                        }

                    }
            } else {
                mutableLiveData.value = DataOrException(
                    data = null,
                    exception = task.exception
                )
            }
        }
        return mutableLiveData
    }

    fun loginUser(
        email: String,
        password: String
    ): MutableLiveData<DataOrException<FirebaseUser, Exception>> {
        val mutableLiveData = MutableLiveData<DataOrException<FirebaseUser, Exception>>()
        auth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                task.result?.user?.let {
                    mutableLiveData.value = DataOrException(
                        data = it
                    )
                }
            } else {
                mutableLiveData.value = DataOrException(
                    exception = task.exception
                )
            }
        }
        return mutableLiveData
    }

    fun isAuthenticated(): Boolean {
        return auth.currentUser != null
    }


}