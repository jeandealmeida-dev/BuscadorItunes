package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.model.MusicEntity
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
) : BaseModel() {

    override var origin: Origin = Origin.REMOTE

    var id: Long? = null
    var name: String? = null
    var isStreamable: Boolean? = null

    var collection: Collection? = null
    var artist: Artist? = null

    override fun equals(other: Any?): Boolean {
        return if (other is MusicEntity) id == other.musicId else false
    }

    fun toEntity(): MusicEntity = MusicEntity(
        trackName,
        artworkUrl,
        releaseDate,
        isStreamable,
        trackTimeMillis,
        previewUrl,
        ds_trackId
    )

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("yyyy").format(releaseDate) else "-"

}