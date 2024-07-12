package com.jeanpaulo.musiclibrary.core.domain.model

import java.text.SimpleDateFormat
import java.util.*

data class Music(
    val id: Long = 0,
    val musicId: Long,
    val trackName: String,
    val artworkUrl: String?,
    val releaseDate: Date?,
    val streamable: Boolean?,
    val trackTimeMillis: Long?,
    val previewUrl: String?
) {
    var musicCollection: Collection? = null
    var musicArtist: Artist? = null

    override fun equals(other: Any?): Boolean {
        return if (other is Music) id == other.id else false
    }

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("yyyy").format(releaseDate) else "-"

}