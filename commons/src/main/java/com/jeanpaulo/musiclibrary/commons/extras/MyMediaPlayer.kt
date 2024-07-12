package com.jeanpaulo.musiclibrary.commons.extras

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

    private var currentUrl: String = ""

    fun setOnCompletionListener(onCompletionListener: () -> Unit) {
        this.onCompletionListener = onCompletionListener
    }

    fun setOnUpdateCounterListener(onUpdateCounterListener: (Long) -> Unit) {
        this.onUpdateCounterListener = onUpdateCounterListener
    }

    private fun getMediaPlayer(): MediaPlayer =
        mediaPlayer ?: run {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(currentUrl)).also {
                it.setOnPreparedListener { player ->
                    Log.d("MyMediaPlayer", "[Player] Ready to play")

                    player.start()
                    updateCounterRunnable.start()
                }

                it.setOnCompletionListener {
                    Log.d("MyMediaPlayer", "[Player] Finish")
                    onCompletionListener?.invoke()
                    updateCounterRunnable.stop()
                }

                it.setOnErrorListener { mp, what, extra ->
                    Log.d("MyMediaPlayer", "[Player] Error: ${what} - ${extra}")
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
        changePlayerUrl(url)
    }

    fun pause() {
        updateCounterRunnable.pause()
        getMediaPlayer().pause()
    }

    fun changeMedia(url: String) {
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

    private fun changePlayerUrl(url: String) {
        getMediaPlayer().let {
            it.reset()
            it.setDataSource(context, Uri.parse(url))
            it.prepareAsync()
        }
    }
}