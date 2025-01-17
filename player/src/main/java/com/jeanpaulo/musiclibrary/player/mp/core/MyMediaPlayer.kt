package com.jeanpaulo.musiclibrary.player.mp.core

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log


class MyMediaPlayer(val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var onCompletionListener: (() -> Unit)? = null
    private var onUpdateCounterListener: ((Long) -> Unit)? = null

    private val updateCounterRunnable = MyMediaPlayerCounter(Handler(Looper.getMainLooper())) {
        onUpdateCounterListener?.invoke(it)
    }

    private val updateVolumeRunnable =
        MyMediaPlayerFade(Handler(Looper.getMainLooper())) { volume ->
            mediaPlayer?.setVolume(volume, volume)
        }

    private var currentUrl: String = ""

    fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        this.onCompletionListener = onCompletionListener
    }

    fun setOnUpdateCounterListener(onUpdateCounterListener: (Long) -> Unit) {
        this.onUpdateCounterListener = onUpdateCounterListener
    }

    private fun getMediaPlayer(): MediaPlayer =
        mediaPlayer ?: run {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(currentUrl)).apply {
                setOnPreparedListener { player ->
                    Log.d(TAG, "[Player] Ready to play")

                    player.start()
                    updateVolumeRunnable.start()
                    updateCounterRunnable.start()
                }

                setOnCompletionListener {
                    Log.d(TAG, "[Player] Finish")
                    onCompletionListener?.invoke()
                    updateCounterRunnable.stop()
                }

                setOnErrorListener { mp, what, extra ->
                    Log.d(TAG, "[Player] Error: ${what} - ${extra}")
                    true
                }
            }
            return@run mediaPlayer!!
        }

    fun playSong(url: String) {
        changeMedia(url)
    }

    fun play() {
        getMediaPlayer().start()
        updateCounterRunnable.play()
    }

    fun next(url: String) {
        changeMedia(url)
    }

    fun pause() {
        updateCounterRunnable.pause()
        getMediaPlayer().pause()
    }

    private fun changeMedia(url: String) {
        currentUrl = url
        getMediaPlayer().let {
            it.reset()
            it.setDataSource(context, Uri.parse(url))
            it.prepareAsync()
        }
    }

    /***
     * Use reset() for frequent media changes
     */
    fun reset() {
        getMediaPlayer().reset()
    }

    /***
     * Calling stop() alone doesn't release resources.
     * Use release() when you're done with the MediaPlayer or switching to a different media source.
     */
    fun stop() {
        getMediaPlayer().let {
            it.pause()
            it.seekTo(0)
            updateVolumeRunnable.stop()
            updateCounterRunnable.stop()
        }
    }

    /***
     * Call release() when:
     * - You're done using the MediaPlayer
     * - You're switching to a different media source:
     */
    fun release() {
        getMediaPlayer().let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    companion object {
        const val TAG = "MyMediaPlayer"
    }
}