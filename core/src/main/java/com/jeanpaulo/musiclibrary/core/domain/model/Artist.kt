package com.jeanpaulo.musiclibrary.core.domain.model

data class Artist(
    val id: Long,
    val name: String,
    val country: String?,
    val primaryGenreName: String?
)