package com.jeanpaulo.buscador_itunes.datasource.remote.json_objects

import com.jeanpaulo.buscador_itunes.model.Artist
import com.jeanpaulo.buscador_itunes.model.Collection
import com.jeanpaulo.buscador_itunes.model.Music
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class MusicJson(
    @Json(name = "trackId") val trackId: Long?,
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

    fun convert(): Music {
        val collection = Collection(collectionId, collectionName, trackCount = trackCount)
        val artist = Artist(artistId, artistName, country, primaryGenreName)
        val music = Music(trackId, trackName, artworkUrl, releaseDate, isStreamable, trackTimeMillis, previewUrl)

        music.collection = collection
        music.artist = artist

        return music
    }
}

//REF: CustomAdapter para clases complexas https://gist.github.com/alexforrester/5c96ace4227916fb456ff49a16ef025d
