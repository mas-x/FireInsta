package com.masrooraijaz.fireinsta

import androidx.appcompat.app.AppCompatActivity
import com.masrooraijaz.fireinsta.util.displayErrorDialog

abstract class BaseActivity : AppCompatActivity() {


    fun handleException(e: Exception) {
        this.displayErrorDialog(
            e.message.toString()
        )
    }

    abstract fun showProgressbar(bool: Boolean)
}