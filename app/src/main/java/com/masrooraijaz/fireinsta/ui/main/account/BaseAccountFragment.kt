package com.masrooraijaz.fireinsta.ui.main.account

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.masrooraijaz.fireinsta.ui.DataListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

private const val TAG = "BaseAccountFragment"

abstract class BaseAccountFragment : Fragment() {
    protected lateinit var dataListener: DataListener
    protected val accountViewModel by sharedViewModel<AccountViewModel>()
    protected val requestManager by inject<RequestManager>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataListener = context as DataListener
        } catch (exception: ClassCastException) {
            exception.printStackTrace()
            Log.d(TAG, "onAttach: Invalid activity... Does not implement Data Listener")
        }
    }
}