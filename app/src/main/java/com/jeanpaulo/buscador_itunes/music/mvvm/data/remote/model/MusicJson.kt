package com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.model

import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Artist
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Collection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class MusicJson(
    @Json(name = "trackId") val ds_trackId: Long?,
    @Json(name = "trackName") val trackName: String?,

    @Json(name = "artistId") val artistId: Long?,
    @Json(name = "artistName") val artistName: String?,

    @Json(name = "collectionId") val collectionId: Long?,
    @Json(name = "collectionName") val collectionName: String?,

    @Json(name = "artworkUrl100") val artworkUrl: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "trackCount") val trackCount: Int?,
    @Json(name = "releaseDate") val releaseDate: Date?,
    @Json(name = "primaryGenreName") val primaryGenreName: String?,
    @Json(name = "isStreamable") val isStreamable: Boolean?,
    @Json(name = "trackTimeMillis") val trackTimeMillis: Long?,
    @Json(name = "previewUrl") val previewUrl: String?
) {

    fun convert() = Music(
        ds_trackId,
        trackName,
        artworkUrl,
        releaseDate,
        isStreamable,
        trackTimeMillis,
        previewUrl
    ).apply {
        this.artist = Artist(artistId, artistName, country, primaryGenreName)
        this.collection = Collection(collectionId, collectionName)
    }
}

//REF: CustomAdapter para clases complexas https://gist.github.com/alexforrester/5c96ace4227916fb456ff49a16ef025d
