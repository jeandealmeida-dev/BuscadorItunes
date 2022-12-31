package com.jeanpaulo.musiclibrary.music.ui.model

import java.util.*

data class MusicDetailUIModel(
    val id: Long,
    val remoteId: Long,
    val name: String,
    val artwork: String,
    val previewURL: String?,
    val artist: String,
    val album: String,
    val releaseDate: Date?,
    val streamable: Boolean?,
    val trackTimeMillis: Long?,
    var isFavorite: Boolean = false
) {
    fun hasPreview() = previewURL != null
    fun getPreview(): String = previewURL ?: ""

    fun isFavorite(boolean: Boolean = true) { isFavorite = boolean }
}