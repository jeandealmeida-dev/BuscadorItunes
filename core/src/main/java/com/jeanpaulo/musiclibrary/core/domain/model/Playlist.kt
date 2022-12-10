package com.jeanpaulo.musiclibrary.core.domain.model

data class Playlist(
    var playlistId: Long = 0,
    var title: String,
    var description: String? = null
)