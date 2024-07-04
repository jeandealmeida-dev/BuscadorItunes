package com.jeanpaulo.musiclibrary.search.ui.model

data class SearchMusicUIModel(
    val musicId: Long,
    val musicName: String,
    val artistName: String,
    val collectionName: String,
    val collectionYear: String,
    val artworkUrl: String,
    val previewUrl: String?
)