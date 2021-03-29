package com.jeanpaulo.buscador_itunes.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri


class MyMediaPlayer(val url: String, val onChangeState: (Boolean) -> Unit) {

    private lateinit var mediaPlayer: MediaPlayer

    fun create(context: Context?) {
        mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
        mediaPlayer.setOnCompletionListener { mp ->
            onChangeState(false)
            mp.prepare()
        }
    }

    fun play() {
        if (!mediaPlayer.isPlaying) {
            mediaPlayer.start()
            onChangeState(true)
        }
    }

    fun pause() {
        mediaPlayer.pause()
        onChangeState(false)
    }


    fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}