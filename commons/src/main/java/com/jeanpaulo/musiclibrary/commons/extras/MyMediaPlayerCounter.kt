package com.jeanpaulo.musiclibrary.commons.extras

import android.os.Handler
import android.util.Log

class MyMediaPlayerCounter(
    val handler: Handler,
    val onUpdate: (Long) -> Unit,
) : Runnable {

    var count = 0L
    var isUpdating = true

    private var isRunningPlayer = false

    fun start() {
        Log.d(TAG, "Start")
        count = 0L

        if (handler.hasCallbacks(this)) {
            handler.removeCallbacks(this)
        }

        isRunningPlayer = true
        isUpdating = true
        handler.post(this)
    }

    fun play() {
        Log.d(TAG, "Play -> count = ${count}s")

        if (!isRunningPlayer) {
            start()
        }
        isUpdating = true
    }

    fun pause() {
        Log.d(TAG, "Pause")
        isUpdating = false
    }

    fun stop() {
        Log.d(TAG, "Stop")
        count = 0L
        isUpdating = false
        isRunningPlayer = false

        handler.removeCallbacks(this)
    }

    override fun run() {
        if (isRunningPlayer) {
            if (isUpdating) {
                onUpdate(count++)
            }
            handler.postDelayed(this, ONE_SECOND)
        }
    }

    companion object {
        const val TAG = "MyMediaPlayerCounter"
        const val ONE_SECOND = 1000L
    }
}