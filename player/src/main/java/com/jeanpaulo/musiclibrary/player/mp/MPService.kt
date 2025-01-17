package com.jeanpaulo.musiclibrary.player.mp

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jeanpaulo.musiclibrary.player.mp.core.MyMediaPlayer
import com.jeanpaulo.musiclibrary.player.mp.MPParams.COMMAND
import com.jeanpaulo.musiclibrary.player.mp.MPParams.COUNTER
import com.jeanpaulo.musiclibrary.player.mp.MPParams.PLAYLIST
import com.jeanpaulo.musiclibrary.player.mp.MPParams.SONG
import com.jeanpaulo.musiclibrary.player.mp.MPParams.STATE
import com.jeanpaulo.musiclibrary.player.mp.model.MPPlaylist
import com.jeanpaulo.musiclibrary.player.mp.model.MPSong
import com.jeanpaulo.musiclibrary.player.mp.model.MPState

class MPService : Service() {

    private var mediaPlayer: MyMediaPlayer? = null
    private var currentPlaylist: MPPlaylist = MPPlaylist(songs = mutableListOf())

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "[Service] Running..")
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        Log.d(TAG, "[Service] Destroyed!")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d(TAG, "OnBind")
        return null
    }

    private fun getMediaPlayer(): MyMediaPlayer =
        mediaPlayer ?: run {
            mediaPlayer = MyMediaPlayer(applicationContext).apply {
                setOnCompletionListener {
                    nextSong()
                }

                setOnUpdateCounterListener {
                    broadcastCounterCommand(it)
                }
            }
            mediaPlayer!!
        }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let { extras ->
            val command = extras.getString(COMMAND) ?: ""
            val playlist = extractPlaylistArgs(extras)

            receivingCommand(command, playlist)
        }
        return START_NOT_STICKY
    }

    private fun extractPlaylistArgs(extras: Bundle) =
        if (extras.containsKey(PLAYLIST)) {
            extras.getParcelable(PLAYLIST) ?: MPPlaylist()
        } else if (extras.containsKey(SONG)) {
            extras.getParcelable<MPSong?>(SONG)?.let { song ->
                MPPlaylist(songs = mutableListOf(song))
            } ?: MPPlaylist()
        } else {
            MPPlaylist()
        }

    private fun receivingCommand(command: String, playlist: MPPlaylist) {
        Log.d(TAG, "[Service] Receiving {$command}..")
        when (command) {
            MPCommands.PLAY,
            MPCommands.PLAY_SONG_LIST,
            MPCommands.PLAY_SONG -> {
                currentPlaylist = playlist

                if (command == MPCommands.PLAY) {
                    getMediaPlayer().play()
                } else {
                    playSong()
                }
                broadcastCommand(command)
            }

            MPCommands.ADD_SONG -> {
                currentPlaylist.songs.addAll(playlist.songs)
                broadcastCommand(command)
            }

            MPCommands.PAUSE -> {
                getMediaPlayer().pause()
                broadcastCommand(command)
            }

            MPCommands.NEXT -> {
                nextSong()
                broadcastCommand(command)
            }

            MPCommands.PREVIOUS -> {
                previousSong()
                broadcastCommand(command)
            }

            MPCommands.STOP -> {
                getMediaPlayer().stop()
                broadcastCommand(command)
            }

            else -> {} //TODO
        }
    }

    private fun broadcastCommand(command: String) {
        Log.d(TAG, "[Broadcast] Sending {$command}..")

        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(Intent(MPReceiver.ACTION).apply {
                putExtra(
                    STATE, MPState(
                        command = command,
                        currentSong = currentPlaylist.play(),
                        hasPrevious = currentPlaylist.hasPrevious(),
                        hasNext = currentPlaylist.hasNext()
                    )
                )
            })
    }

    private fun broadcastCounterCommand(counter: Long) {
        Log.d(TAG, "[Broadcast] Sending counter {$counter}..")

        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(Intent(MPReceiver.ACTION).apply {
                putExtra(COMMAND, MPCommands.UPDATE_COUNTER)
                putExtra(COUNTER, counter)
            })
    }

    // COMMANDS
    private fun playSong() {
        currentPlaylist.play()?.previewUrl?.let { previewUrl ->
            getMediaPlayer().playSong(previewUrl)
        } ?: run {
            nextSong()
        }
    }

    private fun nextSong() {
        if (currentPlaylist.next()) {
            playSong()
            broadcastCommand(MPCommands.PLAY_SONG)
        } else {
            broadcastCommand(MPCommands.STOP)
        }
    }

    private fun previousSong() {
        if (currentPlaylist.previous()) {
            playSong()
            broadcastCommand(MPCommands.PLAY_SONG)
        } else {
            broadcastCommand(MPCommands.STOP)
        }
    }

    companion object {

        const val TAG = "MusicPlayerService"
        private fun createIntent(context: Context): Intent = Intent(context, MPService::class.java)

        // PLAY

        fun play(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PLAY)
            })
        }

        fun playSongList(context: Context, songs: List<MPSong>) {
            val playlist = MPPlaylist(songs = songs.toMutableList())
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, if (songs.size > 1) MPCommands.PLAY_SONG_LIST else MPCommands.PLAY_SONG)
                putExtra(PLAYLIST, playlist)
            })
        }

        // OTHERS COMMANDS

        fun pause(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PAUSE)
            })
        }

        fun stop(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.STOP)
            })
        }

        fun next(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.NEXT)
            })
        }

        fun previous(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PREVIOUS)
            })
        }
    }
}