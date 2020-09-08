package com.masrooraijaz.fireinsta.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.masrooraijaz.fireinsta.R
import kotlinx.android.synthetic.main.fragment_launcher.*
import kotlinx.android.synthetic.main.fragment_launcher.btn_sign_up
import kotlinx.android.synthetic.main.fragment_register.*

private const val TAG = "RegisterFragment"

class RegisterFragment : BaseAuthFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "subscribeObservers: ${viewModel.hashCode()}")
        btn_sign_up.setOnClickListener {

            viewModel.createUser(
                edit_text_display_name.text.toString(),
                edit_text_email_address.text.toString(),
                edit_text_password.text.toString(),
                edit_text_confirm_password.text.toString()
            )

        }


    }

}