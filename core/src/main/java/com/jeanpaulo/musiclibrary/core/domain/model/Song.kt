package com.jeanpaulo.musiclibrary.core.domain.model

import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong

data class Song(
    val musicId: Long,

    val name: String,
    val artist: String,

    val artworkUrl: String?,
    val previewUrl: String?,
) {
    fun toMPSong() = MPSong (
        id = this.musicId,
        name = this.name,
        artist = this.artist,
        artworkUrl = this.artworkUrl,
        previewUrl = this.previewUrl,
    )

    companion object {
        fun fromMPSong(mpSong: MPSong) = Song(
            musicId = mpSong.id,
            name = mpSong.name,
            artist = mpSong.artist,
            artworkUrl = mpSong.artworkUrl,
            previewUrl = mpSong.previewUrl,
        )
    }
}