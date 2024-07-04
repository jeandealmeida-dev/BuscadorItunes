package com.jeanpaulo.musiclibrary.commons.extensions.ui

import android.R
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar


fun Context.showTopSnackbar(view: View, text: String) {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
    val view: View = snackbar.getView()
    val params = view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP

    // calculate actionbar height
    var actionBarHeight = resources
        .getDimensionPixelSize(
            this.getDimenAttr(
                R.attr.actionBarSize
            )
        );

    params.setMargins(0, actionBarHeight, 0, 0)

    view.layoutParams = params
    snackbar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_FADE
    snackbar.show()
}