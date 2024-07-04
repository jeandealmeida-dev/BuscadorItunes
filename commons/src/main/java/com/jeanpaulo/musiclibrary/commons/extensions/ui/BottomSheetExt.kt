package com.jeanpaulo.musiclibrary.commons.extensions.ui

import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

fun View.setFullScreen(windowManager: WindowManager) {
    val childLayoutParams: ViewGroup.LayoutParams = this.layoutParams
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(displayMetrics)

    childLayoutParams.height = displayMetrics.heightPixels

    this.layoutParams = childLayoutParams
}