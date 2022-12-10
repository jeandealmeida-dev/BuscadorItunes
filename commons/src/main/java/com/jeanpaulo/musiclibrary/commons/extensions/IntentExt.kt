package com.jeanpaulo.musiclibrary.commons.extensions

import android.content.Intent
import android.os.Parcelable

fun Intent.requireString(key: String): String =
    getStringExtra(key)!!

fun Intent.requireLong(key: String): Long =
    getLongExtra(key, 0)

inline fun <reified T : Parcelable> Intent.requireParcelable(key: String): T {
    return getParcelableExtra(key)!!
}