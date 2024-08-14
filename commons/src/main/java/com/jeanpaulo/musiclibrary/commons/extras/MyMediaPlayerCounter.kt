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
    private var isRunnablePosted = false

    fun start() {
        Log.d(TAG, "Start")
        count = 0L

        if (isRunnablePosted) {
            handler.removeCallbacks(this)
            isRunnablePosted = false
        }

        isRunningPlayer = true
        isUpdating = true
        handler.post(this)
        isRunnablePosted = true
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
        isRunnablePosted = false
    }

    override fun run() {
        if (isRunningPlayer) {
            if (isUpdating) {
                onUpdate(count++)
            }
            handler.postDelayed(this, ONE_SECOND)
            isRunnablePosted = true
        } else {
            isRunnablePosted = false
        }
    }

    companion object {
        const val TAG = "MyMediaPlayerCounter"
        const val ONE_SECOND = 1000L
    }
}