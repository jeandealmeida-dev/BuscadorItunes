package com.jeanpaulo.musiclibrary.core.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong

class MusicPlayerReceiver() : BroadcastReceiver() {

    private var playerEvents: MusicPlayerEvents? = null

    constructor(playerEvents: MusicPlayerEvents? = null) : this() {
        this.playerEvents = playerEvents
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.extras?.let {
            val command = it.getString(MusicPlayerParams.COMMAND)
            val song = it.getParcelable<MusicPlayerSong>(MusicPlayerParams.SONG)

            when (command) {
                MusicPlayerCommands.PLAY_SONG -> {
                    song?.let { playerEvents?.onPlaySong(song) }
                }

                MusicPlayerCommands.PLAY -> {
                    playerEvents?.onPlay()
                }

                MusicPlayerCommands.PAUSE -> {
                    playerEvents?.onPause()
                }

                MusicPlayerCommands.STOP -> {
                    playerEvents?.onStop()
                }

                else -> {} //TOOD
            }
        }
    }

    companion object {
        const val ACTION = "com.jeanpaulo.intent.action.MUSIC_PLAYER"
    }
}
