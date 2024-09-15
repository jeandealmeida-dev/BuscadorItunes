package com.jeanpaulo.musiclibrary.core.ui.model

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.domain.model.Song

data class SongUIModel(
    val musicId: Long,
    val musicName: String,
    val artistId: Long,
    val artistName: String,
    val collectionId: Long,
    val collectionName: String,
    val artworkUrl: String?,
    val previewUrl: String?
) {

    fun convertToSong() = Song(
        musicId = this.musicId,
        name = this.musicName,
        artist = this.artistName,
        artworkUrl = this.artworkUrl,
        previewUrl = this.previewUrl
    )
    companion object {

        fun fromModel(music: Music) = SongUIModel(
            musicId = music.musicId,
            musicName = music.trackName,
            artworkUrl = music.artworkUrl,
            artistId = music.musicArtist?.artistId ?: -1,
            artistName = music.musicArtist?.name ?: "",
            collectionId = music.musicCollection?.collectionId ?: -1,
            collectionName = music.musicCollection?.name ?: "",
            previewUrl = music.previewUrl
        )
    }
}