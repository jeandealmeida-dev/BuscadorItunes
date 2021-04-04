package com.jeanpaulo.buscador_itunes.model

import com.jeanpaulo.buscador_itunes.datasource.local.entity.MusicEntity
import java.text.SimpleDateFormat
import java.util.*

class Music() {

    constructor(trackId: String?) : this() {
        this.trackId = trackId
    }

    constructor(
        ds_trackId: Long?,
        trackName: String?,
        artworkUrl: String?,
        releaseDate: Date?,
        streamable: Boolean?,
        trackTimeMillis: Long?,
        previewUrl: String?
    ) : this(null) { //Called By MusicJson (so it doesnt have Id)
        this.name = trackName
        this.ds_trackId = ds_trackId
        this.artworkUrl = artworkUrl
        this.releaseDate = releaseDate
        this.isStreamable = streamable
        this.trackTimeMillis = trackTimeMillis
        this.previewUrl = previewUrl
    }

    var trackId: String? = null
    var ds_trackId: Long? = null
    var name: String? = null
    var artworkUrl: String? = null
    var releaseDate: Date? = null
    var isStreamable: Boolean? = null
    var trackTimeMillis: Long? = null
    var previewUrl: String? = null

    var collection: _Collection? = null
    var artist: Artist? = null

    override fun equals(other: Any?): Boolean {
        return if (other is MusicEntity) ds_trackId == other.ds_trackId else false
    }

    fun toEntity(): MusicEntity =
        if (trackId != null) MusicEntity(
            ds_trackId,
            name,
            artworkUrl,
            releaseDate,
            isStreamable,
            trackTimeMillis,
            previewUrl,
            trackId!!
        ) else MusicEntity(
            ds_trackId,
            name,
            artworkUrl,
            releaseDate,
            isStreamable,
            trackTimeMillis,
            previewUrl
        )

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("yyyy").format(releaseDate) else "-"

}