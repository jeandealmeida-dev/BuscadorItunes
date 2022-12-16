package com.jeanpaulo.musiclibrary.search.domain.model

data class SearchMusic(
    val musicId: Long,
    val musicName: String,
    val artistName: String,
    val collectionName: String,
    val collectionYear: String,
    val artworkUrl: String
)