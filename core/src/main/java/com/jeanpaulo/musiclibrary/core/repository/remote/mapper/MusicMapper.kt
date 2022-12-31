package com.jeanpaulo.musiclibrary.core.repository.remote.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse

fun MusicResponse.convertToMusic(): Music {
    val music = Music(
        ds_trackId = this.remoteId ?: 0L,
        trackName = this.trackName ?: "",
        artworkUrl = this.artworkUrl ?: "",
        releaseDate = this.releaseDate,
        trackTimeMillis = this.trackTimeMillis,
        streamable = this.isStreamable,
        previewUrl = this.previewUrl,
    )

    music.musicArtist = Artist(
        id = this.artistId ?: 0,
        name = this.artistName ?: "",
        country = this.country,
        primaryGenreName = this.primaryGenreName,
    )

    music.musicCollection = Collection(
        id = this.collectionId ?: 0,
        name = this.collectionName ?: ""
    )

    return music
}