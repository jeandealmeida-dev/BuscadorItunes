package com.jeanpaulo.musiclibrary.core.service

import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.jeanpaulo.musiclibrary.commons.extras.MyMediaPlayer
import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong
import com.jeanpaulo.musiclibrary.core.service.MusicPlayerParams.COMMAND
import com.jeanpaulo.musiclibrary.core.service.MusicPlayerParams.SONG

class MusicPlayerService : Service() {

    private var mediaPlayer: MyMediaPlayer? = null
    private var playlist: MutableList<MusicPlayerSong> = mutableListOf()

    private var currentSong: MusicPlayerSong? = null

    // Lifecycle Service
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("MusicPlayerService", "[Service] Running..")
        setupMusicPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let { extras ->
            val command = extras.getString(COMMAND) ?: ""
            val song = extras.getParcelable<MusicPlayerSong>(SONG)
            processing(command, song)

            LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(Intent(MusicPlayerReceiver.ACTION).apply {
                    putExtra(COMMAND, command)
                    putExtra(SONG, song)
                })

            Log.d("MusicPlayerService", "[Broadcast] Sending {$command}..")
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
        Log.d("MusicPlayerService", "[Service] Destroyed!")
    }

    fun setupMusicPlayer() {
        mediaPlayer = MyMediaPlayer(applicationContext)
        mediaPlayer?.setOnCompletionListener {
            LocalBroadcastManager.getInstance(applicationContext)
                .sendBroadcast(Intent(MusicPlayerReceiver.ACTION).apply {
                    putExtra(COMMAND, MusicPlayerCommands.STOP)
                })
        }
    }

    private fun processing(command: String, song: MusicPlayerSong? = null) {
        Log.d("MusicPlayerService", "[Service] Receiving {$command}..")
        when (command) {
            MusicPlayerCommands.PLAY -> {
                play()
            }

            MusicPlayerCommands.PLAY_SONG -> {
                playSong(song)
            }

            MusicPlayerCommands.PLAY_SONG -> {
                stop()
            }

            MusicPlayerCommands.ADD_SONG -> {
                addSong(song)
            }

            MusicPlayerCommands.PAUSE -> {
                pause()
            }

            MusicPlayerCommands.NEXT -> {
                nextSong()
            }

            else -> {} //TODO
        }
    }

    // COMMANDS
    private fun playSong(song: MusicPlayerSong?) {
        song?.let { song ->
            currentSong = song
            mediaPlayer?.let {
                it.playSong(song.previewUrl)
            }
        } ?: run {
            throw Exception("If you would like to play a song, please specify it!")
        }
    }

    private fun play() {
        currentSong?.let {
            mediaPlayer?.play()
        } ?: run {
            throw Exception("No current song is playing")
        }
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun stop() {
        mediaPlayer?.stop()
    }

    private fun nextSong() {
        val song = playlist.first()
        playlist.removeFirst()
        playSong(song)
    }

    private fun addSong(song: MusicPlayerSong?) {
        song?.let { song ->
            playlist.add(song)
        } ?: run {
            throw Exception("If you would like to add a song, please specify it!")
        }
    }

    companion object {
        fun playASong(context: Context, song: MusicPlayerSong) {
            context.startService(
                Intent(
                    context,
                    MusicPlayerService::class.java
                ).apply {
                    this.putExtra(
                        COMMAND,
                        MusicPlayerCommands.PLAY_SONG
                    )
                    this.putExtra(SONG, song)
                }
            )
        }

        fun play(context: Context) {
            context.startService(
                Intent(
                    context,
                    MusicPlayerService::class.java
                ).apply {
                    this.putExtra(
                        COMMAND,
                        MusicPlayerCommands.PLAY
                    )
                }
            )
        }

        fun pause(context: Context) {
            context.startService(
                Intent(
                    context,
                    MusicPlayerService::class.java
                ).apply {
                    this.putExtra(
                        COMMAND,
                        MusicPlayerCommands.PAUSE
                    )
                }
            )
        }

        fun stop(context: Context) {
            context.startService(
                Intent(
                    context,
                    MusicPlayerService::class.java
                ).apply {
                    this.putExtra(
                        COMMAND,
                        MusicPlayerCommands.STOP
                    )
                }
            )
        }
    }
}