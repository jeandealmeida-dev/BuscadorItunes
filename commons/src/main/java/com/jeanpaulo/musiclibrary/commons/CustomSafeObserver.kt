package com.jeanpaulo.musiclibrary.commons

import androidx.lifecycle.Observer

class CustomSafeObserver<T>(private val callback: (T) -> Unit) : Observer<T> {
    override fun onChanged(t: T) {
        t?.let { callback(it) }
    }
}