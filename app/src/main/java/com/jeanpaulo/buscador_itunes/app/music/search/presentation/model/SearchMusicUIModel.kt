package com.jeanpaulo.buscador_itunes.app.music.search.presentation.model

data class SearchMusicUIModel(
    val musicId: Long,
    val musicName: String,
    val artistName: String,
    val collectionName: String,
    val collectionYear: String,
    val artworkUrl: String
)