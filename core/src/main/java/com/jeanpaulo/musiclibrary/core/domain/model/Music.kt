package com.jeanpaulo.musiclibrary.core.domain.model

import java.text.SimpleDateFormat
import java.util.*

class Music(
    val ds_trackId: Long?,
    val trackName: String?,
    val artworkUrl: String?,
    val releaseDate: Date?,
    val streamable: Boolean?,
    val trackTimeMillis: Long?,
    val previewUrl: String?
) {

    var id: Long = 0
    var name: String? = null
    var isStreamable: Boolean? = null

    var collection: Collection? = null
    var artist: Artist? = null

    var isFavorited: Boolean = false

    override fun equals(other: Any?): Boolean {
        return if (other is Music) id == other.id else false
    }

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("yyyy").format(releaseDate) else "-"

}