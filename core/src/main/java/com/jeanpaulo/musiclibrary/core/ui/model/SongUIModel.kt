package com.jeanpaulo.musiclibrary.core.ui.model

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.domain.model.Song

data class SongUIModel(
    val musicId: Long,
    val musicName: String,
    val artistName: String,
    val collectionName: String,
    val collectionYear: String,
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
        const val NO_COLLECTION_NAME = "-"
        const val NO_ARTIST_NAME = "-"

        fun fromModel(music: Music) = SongUIModel(
            musicId = music.musicId,
            musicName = music.trackName,
            artworkUrl = music.artworkUrl,
            artistName = music.musicArtist?.name ?: NO_ARTIST_NAME,
            collectionName = music.musicCollection?.name ?: NO_COLLECTION_NAME,
            collectionYear = music.formatedReleaseDate,
            previewUrl = music.previewUrl
        )
    }
}