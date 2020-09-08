package com.masrooraijaz.fireinsta.ui.main.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.RequestManager
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.DataListener
import com.masrooraijaz.fireinsta.ui.main.StatesListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "BaseHomeFragment"

abstract class BaseHomeFragment : Fragment() {

    protected lateinit var dataListener: DataListener
    protected lateinit var statesListener: StatesListener
    protected val homeViewModel by sharedViewModel<HomeViewModel>()
    protected val requestManager by inject<RequestManager>()


    // TODO: 9/6/2020 change comment font. currently looks ugly. 

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            dataListener = context as DataListener
            statesListener = context as StatesListener
        } catch (exception: ClassCastException) {
            exception.printStackTrace()
            Log.d(
                TAG,
                "onAttach: Invalid activity... Does not implement Data Listener & StatesListener"
            )
        }
    }

}