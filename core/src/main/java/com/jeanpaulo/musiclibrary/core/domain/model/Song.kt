package com.jeanpaulo.musiclibrary.core.domain.model

import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong

data class Song(
    val musicId: Long,
    val artistId: Long,

    val name: String,
    val artist: String,

    val artworkUrl: String?,
) {
    companion object {
        fun fromMPSong(mpSong: MPSong) = Song(
            musicId = mpSong.id,
            artistId = 0L,
            name = mpSong.name,
            artist = mpSong.artist,
            artworkUrl = mpSong.artworkUrl,
        )
    }
}