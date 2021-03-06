package com.masrooraijaz.fireinsta.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.masrooraijaz.fireinsta.R
import kotlinx.android.synthetic.main.fragment_launcher.*

private const val TAG = "LauncherFragment"
class LauncherFragment : BaseAuthFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_sign_in.setOnClickListener {
            findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
        }

        btn_sign_up.setOnClickListener {

            findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)

        }
    }


}