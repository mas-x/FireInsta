package com.masrooraijaz.fireinsta.util

import java.lang.Exception
import java.lang.IllegalArgumentException

data class DataOrException<out T, out E : Exception?>(
    val data: T? = null,
    val exception: E? = null,
    val loading: Boolean = false
) {
}
