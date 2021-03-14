package com.jeanpaulo.buscador_itunes.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Collection(
    @Json(name = "collectionId") val id: Long,
    @Json(name = "collectionName") val name: String?
){

    @Json(name = "artistName")
    var artistName: String? = null

    @Json(name = "artworkUrl100")
    var artwork: String? = null

    @Json(name = "trackCount")
    var trackCount: Int = 0

    @Json(name = "country")
    var country: String? = null

    @Json(name = "releaseDate")
    var releaseDate: String? = null

    @Json(name = "primaryGenreName")
    var genre: String? = null

    val tracks: List<Track> = listOf()
}