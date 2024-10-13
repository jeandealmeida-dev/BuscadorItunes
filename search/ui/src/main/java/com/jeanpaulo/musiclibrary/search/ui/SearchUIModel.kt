package com.jeanpaulo.musiclibrary.search.ui

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.domain.model.Song
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel

data class SearchUIModel(
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

    fun convertToSongUIModel() = SongUIModel(
        musicId = this.musicId,
        musicName = this.musicName,
        artistId = this.artistId,
        artistName = this.artistName,
        collectionId = this.collectionId,
        collectionName = this.collectionName,
        artworkUrl = this.artworkUrl,
        previewUrl = this.previewUrl
    )

    companion object {

        fun fromModel(music: Music): SearchUIModel {
            return SearchUIModel(
                musicId = music.musicId,
                musicName = music.trackName,
                artistId = music.musicArtist?.artistId ?: -1,
                artistName = music.musicArtist?.name ?: "",
                collectionId = music.musicCollection?.collectionId ?: -1,
                collectionName = music.musicCollection?.name ?: "",
                artworkUrl = music.artworkUrl,
                previewUrl = music.previewUrl
            )
        }
    }
}