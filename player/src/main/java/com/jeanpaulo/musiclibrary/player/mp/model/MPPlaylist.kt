package com.jeanpaulo.musiclibrary.player.mp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPPlaylist(
    val songs: MutableList<MPSong> = mutableListOf(),
) : Parcelable {

    private val playedSongs: MutableList<MPSong> = mutableListOf<MPSong>()
    private var currentSong: MPSong? = null

    fun play(): MPSong? {
        return currentSong ?: if (songs.isNotEmpty()) {
            currentSong = songs[0]
            currentSong?.also { song ->
                playedSongs.add(song)
            }
        } else null
    }

    fun hasNext() : Boolean {
        val currentIndex = songs.indexOf(currentSong)
        return currentIndex + 1 < songs.size
    }

    fun hasPrevious() = playedSongs.size > 1

    fun previous(): Boolean {
        if (playedSongs.size <= 1) {
            return false
        }
        playedSongs.removeLast()
        currentSong = playedSongs.last()
        return true
    }

    fun next(): Boolean {
        val currentIndex = songs.indexOf(currentSong)
        return if (hasNext()) {
            currentSong = songs[currentIndex + 1]
            playedSongs.add(currentSong!!)
            true
        } else {
            false
        }
    }

    fun add(song: MPSong) {
        songs.add(song)
    }
}