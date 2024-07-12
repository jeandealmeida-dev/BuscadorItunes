package com.jeanpaulo.musiclibrary.search.domain.model

import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse

data class SearchMusic(
    val musicId: Long,
    val musicName: String,
    val artistName: String,
    val collectionName: String,
    val collectionYear: String,
    val artworkUrl: String,
    val previewUrl: String?
) {
    fun MusicResponse.toSearchMusic() =
        SearchMusic(
            musicId = this.remoteId ?: 0L,
            musicName = this.trackName ?: "",
            artworkUrl = this.artworkUrl ?: "",
            artistName = this.artistName ?: "",
            collectionName = this.collectionName ?: "",
            collectionYear = this.releaseDate?.year.toString(),
            previewUrl = this.previewUrl
        )
}