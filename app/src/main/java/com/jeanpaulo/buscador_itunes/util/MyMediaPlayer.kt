package com.jeanpaulo.buscador_itunes.util

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri


class MyMediaPlayer {

    private val mpSet = HashSet<MediaPlayer>()

    fun play(context: Context?, uri: String) {
        val mp = MediaPlayer.create(context, Uri.parse(uri))
        mp.setOnCompletionListener { mp ->
            mpSet.remove(mp)
            mp.stop()
            mp.release()
        }
        mpSet.add(mp)
        mp.start()
    }

    fun stop() {
        for (mp in mpSet) {
            if (mp != null) {
                mp.stop()
                mp.release()
            }
        }
        mpSet.clear()
    }
}