package com.jeanpaulo.buscador_itunes.model

import com.jeanpaulo.buscador_itunes.datasource.local.entity.ArtistEntity
import com.jeanpaulo.buscador_itunes.datasource.local.entity.CollectionEntity
import com.jeanpaulo.buscador_itunes.datasource.local.entity.MusicEntity
import java.text.SimpleDateFormat
import java.util.*

class Music() : BaseModel() {

    constructor(trackId: Long?) : this() {
        this.id = trackId
    }

    //CALLED BY REMOTE MUSICJSON
    constructor(
        ds_trackId: Long?,
        trackName: String?,
        artworkUrl: String?,
        releaseDate: Date?,
        streamable: Boolean?,
        trackTimeMillis: Long?,
        previewUrl: String?
    ) : this(ds_trackId) { //Called By MusicJson (so it doesnt have Id)
        this.name = trackName
        this.artworkUrl = artworkUrl
        this.releaseDate = releaseDate
        this.isStreamable = streamable
        this.trackTimeMillis = trackTimeMillis
        this.previewUrl = previewUrl

        this.origin = Origin.REMOTE
    }

    var id: Long? = null
    var name: String? = null
    var artworkUrl: String? = null
    var releaseDate: Date? = null
    var isStreamable: Boolean? = null
    var trackTimeMillis: Long? = null
    var previewUrl: String? = null

    var collection: Collection? = null
    var artist: Artist? = null

    override lateinit var origin: Origin

    override fun equals(other: Any?): Boolean {
        return if (other is MusicEntity) id == other.musicId else false
    }

    fun toEntity(): MusicEntity = MusicEntity(
        name,
        artworkUrl,
        releaseDate,
        isStreamable,
        trackTimeMillis,
        previewUrl,
        id!!
    )

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("yyyy").format(releaseDate) else "-"

}