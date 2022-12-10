package com.jeanpaulo.musiclibrary.core.repository.remote.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse

fun MusicResponse.convertToMusic() = Music(
    ds_trackId = this.ds_trackId ?: 0L,
    trackName = this.trackName ?: "",
    artworkUrl = this.artworkUrl ?: "",
    releaseDate = this.releaseDate,
    trackTimeMillis = this.trackTimeMillis,
    streamable = this.isStreamable,
    previewUrl = this.previewUrl
)