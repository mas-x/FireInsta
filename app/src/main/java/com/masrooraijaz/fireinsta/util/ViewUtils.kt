package com.masrooraijaz.fireinsta.util

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.masrooraijaz.fireinsta.R

fun Context.displayErrorDialog(msg: String) {

    MaterialDialog(this).show {
        title(R.string.text_error)
        message(text = msg)
        positiveButton(R.string.text_ok)
    }
}
