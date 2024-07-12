package com.jeanpaulo.musiclibrary.core.music_player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import com.jeanpaulo.musiclibrary.core.music_player.model.MPState

class MPReceiver<T>(val cast: (MPSong) -> T) : BroadcastReceiver() {

    private var playerEvents: MPEvents<T>? = null

    constructor(cast: (MPSong) -> T, playerEvents: MPEvents<T>? = null) : this(cast) {
        this.playerEvents = playerEvents
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MPReceiver", "Receiving new broadcast command")
        intent?.extras?.let {
            val state = it.getParcelable<MPState>(MPParams.STATE)
            val command = state?.command ?: it.getString(MPParams.COMMAND)

            when (command) {
                MPCommands.PLAY_SONG,
                MPCommands.PLAY_SONG_LIST -> {
                    state?.currentSong?.let { song ->
                        playerEvents?.onPlaySong(
                            currentSong = cast(song),
                            hasPrevious = state.hasPrevious,
                            hasNext = state.hasNext,
                        )
                    }
                }

                MPCommands.PLAY -> {
                    playerEvents?.onPlay()
                }

                MPCommands.PAUSE -> {
                    playerEvents?.onPause()
                }

                MPCommands.STOP -> {
                    playerEvents?.onStop()
                }

                MPCommands.UPDATE_COUNTER -> {
                    val counter = it.getLong(MPParams.COUNTER)
                    playerEvents?.onUpdateCounter(counter)
                }


                else -> {} //TOOD
            }
        }
    }

    companion object {
        const val ACTION = "com.jeanpaulo.intent.action.MUSIC_PLAYER"
    }
}
