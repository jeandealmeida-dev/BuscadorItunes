package com.jeanpaulo.musiclibrary.commons.extras

import android.os.Handler
import android.util.Log

class MyMediaPlayerFade(
    val handler: Handler,
    val onUpdate: (Float) -> Unit,
) : Runnable {

    private var volume = 0f
    private var isPlaying = false

    fun start() {
        Log.d(TAG, "Start")
        volume = 0f
        isPlaying = true
        handler.post(this)
    }

    fun stop() {
        Log.d(TAG, "Stop")
        volume = 1f
        isPlaying = false
        handler.post(this)
    }

    override fun run() {
        if (isPlaying) {
            fadeIn()
        } else {
            fadeOut()
        }
    }

    private fun fadeIn() {
        volume += 1f / (FADE_DURATION / FADE_INTERVAL)
        if (volume <= 1f) {
            onUpdate(volume)
            handler.postDelayed(this, FADE_INTERVAL)
        } else {
            Log.d(TAG, "Stop")
            onUpdate(1f)
        }
    }

    private fun fadeOut() {
        volume -= 1f / (FADE_DURATION / FADE_INTERVAL)
        if (volume >= 0f) {
            onUpdate(volume)
            handler.postDelayed(this, FADE_INTERVAL)
        } else {
            onUpdate(0f)
        }
    }

    companion object {
        private const val TAG = "MyMediaPlayerFade"

        private const val FADE_DURATION = 1000L // duração do fade-in em milissegundos
        private const val FADE_INTERVAL = 100L // intervalo de aumento do volume em milissegundos
    }
}