package com.masrooraijaz.fireinsta.util

import android.content.Context
import android.util.DisplayMetrics

object LayoutUtil {
    @JvmStatic
    fun calculateNoOfColumns(
        context: Context,
        columnWidth: Float
    ): Int {

        val displayMetrics: DisplayMetrics = context.resources.displayMetrics
        val columnWidthDp = columnWidth / displayMetrics.density
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }
}