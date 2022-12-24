package com.jeanpaulo.musiclibrary.music.ui.model

data class MusicDetailUIModel(
    val id: Long,
    val name: String,
    val artwork: String,
    val previewURL: String?,
    val artist: String,
    val album: String,
) {
    fun hasPreview() = previewURL != null
    fun getPreview(): String = previewURL ?: ""
}