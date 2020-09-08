package com.masrooraijaz.fireinsta.ui

import com.masrooraijaz.fireinsta.util.DataOrException

interface DataListener {
    fun handleDataChange(dataOrException: DataOrException<*, *>)
}