package com.masrooraijaz.fireinsta.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.masrooraijaz.fireinsta.BaseActivity
import com.masrooraijaz.fireinsta.R
import com.masrooraijaz.fireinsta.ui.main.MainActivity
import com.masrooraijaz.fireinsta.util.displayErrorDialog
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "MainActivity"

class AuthActivity : BaseActivity() {

    private val authViewModel by viewModel<AuthViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        subscribeObservers()
        if (authViewModel.isAuthenticated())
            navMainActivity()
    }

    private fun subscribeObservers() {
        Log.d(TAG, "subscribeObservers: ${authViewModel.hashCode()}")
        authViewModel.user.observe(this, Observer { data ->
            showProgressbar(data.loading)
            data.data?.let {
                navMainActivity()
                Toast.makeText(this, "Successfully logged in...", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "subscribeObservers: Logged in....")
            }
            data.exception?.let {
                handleException(it)
            }
            Log.d(TAG, "subscribeObservers: ${data.data}")
        })

        authViewModel.errorLiveData.observe(this, Observer {
            it?.let {
                this.displayErrorDialog(it)
            }

        })
    }

    override fun showProgressbar(boolean: Boolean) {
        if (boolean)
            auth_progress.visibility = View.VISIBLE
        else
            auth_progress.visibility = View.GONE
    }

    private fun navMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}