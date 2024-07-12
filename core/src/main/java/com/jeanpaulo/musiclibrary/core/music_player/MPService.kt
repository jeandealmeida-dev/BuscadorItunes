package com.jeanpaulo.musiclibrary.core.music_player

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jeanpaulo.musiclibrary.commons.extras.MyMediaPlayer
import com.jeanpaulo.musiclibrary.core.music_player.model.MPPlaylist
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.COMMAND
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.COUNTER
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.PLAYLIST
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.SONGG
import com.jeanpaulo.musiclibrary.core.music_player.MPParams.STATE
import com.jeanpaulo.musiclibrary.core.music_player.model.MPState

class MPService : Service() {

    private var mediaPlayer: MyMediaPlayer? = null
    private var currentPlaylist: MPPlaylist = MPPlaylist(songs = mutableListOf())

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicPlayerService", "[Service] Running..")
        setupMusicPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        Log.d("MusicPlayerService", "[Service] Destroyed!")
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        Log.d("MusicPlayerService", "OnBind: Commando ${intent?.extras?.getString(COMMAND) ?: ""}")
    }

    private fun setupMusicPlayer() {
        mediaPlayer = MyMediaPlayer(applicationContext)
        mediaPlayer?.setOnCompletionListener {
            nextSong()
        }
        mediaPlayer?.setOnUpdateCounterListener {
            sendCounterCommand(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let { extras ->
            val command = extras.getString(COMMAND) ?: ""
            val playlist = extractPlaylistArgs(extras)

            processing(command, playlist)
            sendCommand(command)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    fun extractPlaylistArgs(extras: Bundle) = if (extras.containsKey(PLAYLIST)) {
        extras.getParcelable<MPPlaylist>(PLAYLIST)!!
    } else if (extras.containsKey(SONGG)) {
        val song = extras.getParcelable<MPSong>(SONGG)!!
        MPPlaylist(songs = mutableListOf(song))
    } else {
        MPPlaylist()
    }

    private fun processing(command: String, playlist: MPPlaylist) {
        Log.d("MusicPlayerService", "[Service] Receiving {$command}..")
        when (command) {
            MPCommands.PLAY -> {
                currentPlaylist = playlist
                play()
            }

            MPCommands.PLAY_SONG_LIST,
            MPCommands.PLAY_SONG -> {
                currentPlaylist = playlist
                playSong()
            }

            MPCommands.ADD_SONG -> {
                playlist.songs.map {
                    currentPlaylist.add(it)
                }
            }

            MPCommands.PAUSE -> {
                pause()
            }

            MPCommands.NEXT -> {
                nextSong()
            }

            MPCommands.PREVIOUS -> {
                previousSong()
            }

            MPCommands.STOP -> {
                stop()
            }

            else -> {} //TODO
        }
    }

    private fun sendCommand(command: String) {
        Log.d("MusicPlayerService", "[Broadcast] Sending {$command}..")

        val intent = Intent(MPReceiver.ACTION)
        intent.putExtra(
            STATE, MPState(
                command = command,
                currentSong = currentPlaylist.play(),
                hasPrevious = currentPlaylist.hasPrevious(),
                hasNext = currentPlaylist.hasNext()
            )
        )

        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(intent)
    }

    private fun sendCounterCommand(counter: Long) {
        Log.d("MusicPlayerService", "[Broadcast] Sending counter {$counter}..")

        val intent = Intent(MPReceiver.ACTION)
        intent.putExtra(COMMAND, MPCommands.UPDATE_COUNTER)
        intent.putExtra(COUNTER, counter)

        LocalBroadcastManager.getInstance(applicationContext)
            .sendBroadcast(intent)
    }

    // COMMANDS
    private fun playSong() {
        mediaPlayer?.let { mediaPlayer ->
            currentPlaylist.play()?.previewUrl?.let { previewUrl ->
                mediaPlayer.playSong(previewUrl)
            } ?: run {
                nextSong()
            }
        }
    }

    private fun play() {
        mediaPlayer?.let { mediaPlayer ->
            mediaPlayer.play()
        }
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun stop() {
        mediaPlayer?.stop()
    }

    private fun nextSong() {
        if (currentPlaylist.next()) {
            playSong()
            sendCommand(MPCommands.PLAY_SONG)
        } else {
            sendCommand(MPCommands.STOP)
        }
    }

    private fun previousSong() {
        if (currentPlaylist.previous()) {
            playSong()
            sendCommand(MPCommands.PLAY_SONG)
        } else {
            sendCommand(MPCommands.STOP)
        }
    }

    companion object {
        fun playASong(context: Context, song: MPSong) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.PLAY_SONG)
                this.putExtra(PLAYLIST, MPPlaylist(songs = mutableListOf(song)))
            })
        }

        fun play(context: Context) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.PLAY)
            })
        }

        fun pause(context: Context) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.PAUSE)
            })
        }

        fun stop(context: Context) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.STOP)
            })
        }

        fun playSongList(context: Context, songs: MPPlaylist) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.PLAY_SONG_LIST)
                this.putExtra(PLAYLIST, songs)
            })
        }

        fun next(context: Context) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.NEXT)
            })
        }

        fun previous(context: Context) {
            context.startService(Intent(
                context, MPService::class.java
            ).apply {
                this.putExtra(COMMAND, MPCommands.PREVIOUS)
            })
        }
    }
}