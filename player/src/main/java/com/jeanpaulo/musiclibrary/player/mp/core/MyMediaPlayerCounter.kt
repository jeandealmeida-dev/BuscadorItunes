package com.jeanpaulo.musiclibrary.player.mp.core

import android.os.Handler
import android.util.Log
import com.jeanpaulo.musiclibrary.commons.BuildConfig

class MyMediaPlayerCounter(
    val handler: Handler,
    val onUpdate: (Long) -> Unit,
) : Runnable {

    var count = 0L
    var isUpdating = true

    private var isRunningPlayer = false
    private var isRunnablePosted = false

    fun start() {
        log_d("Start")
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
        log_d("Play -> count = ${count}s")

        if (!isRunningPlayer) {
            start()
        }
        isUpdating = true
    }

    fun pause() {
        log_d("Pause")
        isUpdating = false
    }

    fun stop() {
        log_d("Stop")
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

    private fun log_d(text: String){
        if(BuildConfig.DEBUG){
            Log.d(TAG, text)
        }
    }

    companion object {
        const val TAG = "MyMediaPlayerCounter"
        const val ONE_SECOND = 1000L
    }
}