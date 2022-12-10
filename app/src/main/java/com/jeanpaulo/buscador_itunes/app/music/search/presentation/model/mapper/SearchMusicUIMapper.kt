package com.jeanpaulo.buscador_itunes.app.music.search.presentation.model.mapper

import com.jeanpaulo.buscador_itunes.app.music.search.presentation.model.SearchMusicUIModel
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse

fun MusicResponse.toUIModel() = SearchMusicUIModel(
    musicId = this.ds_trackId ?: 0L,
    musicName = this.trackName ?: "",
    artworkUrl = this.artworkUrl ?: "",
    artistName = this.artistName ?: "",
    collectionName = this.collectionName ?: "",
    collectionYear = this.releaseDate?.year.toString()
)