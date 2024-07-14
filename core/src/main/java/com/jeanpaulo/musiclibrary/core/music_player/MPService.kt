package com.jeanpaulo.musiclibrary.core.music_player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jeanpaulo.musiclibrary.commons.extras.MyMediaPlayer
import com.jeanpaulo.musiclibrary.core.domain.model.Song
import com.jeanpaulo.musiclibrary.core.music_player.model.MPPlaylist
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.COMMAND
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.COUNTER
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.PLAYLIST
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.SONG
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.STATE
import com.jeanpaulo.musiclibrary.core.music_player.model.MPState

class MPService : Service() {

    private var mediaPlayer: MyMediaPlayer? = null
    private var currentPlaylist: MPPlaylist = MPPlaylist(songs = mutableListOf())

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicPlayerService", "[Service] Running..")
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        Log.d("MusicPlayerService", "[Service] Destroyed!")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("MusicPlayerService", "OnBind")
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
            extras.getParcelable(PLAYLIST)!!
        } else if (extras.containsKey(SONG)) {
            val song = extras.getParcelable<MPSong>(SONG)!!
            MPPlaylist(songs = mutableListOf(song))
        } else {
            MPPlaylist()
        }

    private fun receivingCommand(command: String, playlist: MPPlaylist) {
        Log.d("MusicPlayerService", "[Service] Receiving {$command}..")
        when (command) {
            MPCommands.PLAY -> {
                currentPlaylist = playlist
                getMediaPlayer().play()
                broadcastCommand(command)
            }

            MPCommands.PLAY_SONG_LIST,
            MPCommands.PLAY_SONG -> {
                currentPlaylist = playlist
                playSong()
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
        Log.d("MusicPlayerService", "[Broadcast] Sending {$command}..")

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
        Log.d("MusicPlayerService", "[Broadcast] Sending counter {$counter}..")

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

        private fun createIntent(context: Context): Intent = Intent(context, MPService::class.java)

        // PLAY

        fun play(context: Context) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PLAY)
            })
        }

        fun playSong(context: Context, song: Song) {
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PLAY_SONG)
                putExtra(PLAYLIST, MPPlaylist(songs = mutableListOf(song.toMPSong())))
            })
        }

        fun playSongList(context: Context, songs: List<Song>) {
            val playlist = MPPlaylist(songs = songs.map { it.toMPSong() }.toMutableList())
            context.startService(createIntent(context).apply {
                putExtra(COMMAND, MPCommands.PLAY_SONG_LIST)
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