package com.masrooraijaz.fireinsta.ui.auth

import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class BaseAuthFragment : Fragment() {

    protected val viewModel by sharedViewModel<AuthViewModel>()

}