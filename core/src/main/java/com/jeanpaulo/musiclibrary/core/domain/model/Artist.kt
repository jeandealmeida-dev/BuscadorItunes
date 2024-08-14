package com.jeanpaulo.musiclibrary.core.domain.model

data class Artist(
    val artistId: Long,
    val name: String,
    val country: String? = null,
    val primaryGenreName: String? = null
)