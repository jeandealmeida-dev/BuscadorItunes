package com.jeanpaulo.musiclibrary.commons.extras

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri


class MyMediaPlayer(val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var previousList: MutableList<String> = mutableListOf()

    fun play(url: String) {
        previousList.clear()

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
            mediaPlayer?.setOnPreparedListener {
                it.start()
            }
        } else {
            changePlayerUrl(url)
        }

        previousList.add(url)
    }

    fun next(url: String) {
        checkPlayerNull()
        changePlayerUrl(url)

        previousList.add(url)
    }

    fun pause() {
        checkPlayerNull()
        mediaPlayer?.pause()
    }

    fun changeMedia(url: String) {
        checkPlayerNull()
        mediaPlayer?.let {
            it.setDataSource(context, Uri.parse(url))
            it.prepareAsync()
        }
    }

    /***
     * Use reset() for frequent media changes
     */
    fun reset() {
        checkPlayerNull()
        mediaPlayer?.reset()
    }

    /***
     * Calling stop() alone doesn't release resources.
     * Use release() when you're done with the MediaPlayer or switching to a different media source.
     */
    fun stop() {
        checkPlayerNull()

        mediaPlayer?.let {
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
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        mediaPlayer = null
    }

    private fun changePlayerUrl(url: String) {
        mediaPlayer?.let {
            it.reset()
            it.setDataSource(context, Uri.parse(url))
            it.prepareAsync()
        }
    }

    private fun checkPlayerNull() {
        if (mediaPlayer == null)
            throw Exception("You should call build before to use it!")
    }
}