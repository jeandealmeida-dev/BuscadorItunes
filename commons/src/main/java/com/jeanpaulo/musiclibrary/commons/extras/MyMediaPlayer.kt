package com.jeanpaulo.musiclibrary.commons.extras

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri


class MyMediaPlayer(val context: Context, var url: String) {

    private lateinit var mediaPlayer: MediaPlayer

    init {
        create()
    }

    fun initialize(){

    }

    fun setPreviewUrl(url: String){
        this.url = url
        create()
    }

    private fun create() {
        mediaPlayer = MediaPlayer.create(context, Uri.parse(url))
        mediaPlayer.setOnCompletionListener { mp ->
            mp.prepare()
        }
    }

    fun play() {
        if (mediaPlayer.isPlaying) {
            release()
            create()
        }
        mediaPlayer.start()
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun stop() {
        release()
    }

    private fun release() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}