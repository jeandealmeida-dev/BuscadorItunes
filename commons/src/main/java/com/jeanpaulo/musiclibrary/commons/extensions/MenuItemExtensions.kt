package com.jeanpaulo.musiclibrary.commons.extensions

import android.graphics.drawable.StateListDrawable
import android.view.MenuItem

fun MenuItem.menuChecked(boolean: Boolean) {
    isChecked = boolean
    val stateListDrawable = icon as StateListDrawable
    val state = intArrayOf(
        if (boolean) {
            android.R.attr.state_checked
        } else{
            android.R.attr.state_empty
        }
    )
    stateListDrawable.state = state

    icon = stateListDrawable.current
}