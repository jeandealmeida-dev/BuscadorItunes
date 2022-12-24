package com.jeanpaulo.musiclibrary.search.domain.model.mapper

import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import com.jeanpaulo.musiclibrary.search.domain.model.SearchMusic

fun MusicResponse.toSearchMusic() =
    SearchMusic(
        musicId = this.ds_trackId ?: 0L,
        musicName = this.trackName ?: "",
        artworkUrl = this.artworkUrl ?: "",
        artistName = this.artistName ?: "",
        collectionName = this.collectionName ?: "",
        collectionYear = this.releaseDate?.year.toString()
    )