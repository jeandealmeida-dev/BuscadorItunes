package com.jeanpaulo.buscador_itunes.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Music(
    @Json(name = "trackId") val id: String,
    @Json(name = "trackName") val name: String,
    @Json(name = "artworkUrl100") val artworkUrl: String,



    @Json(name = "artistId") val artistId: String,
    @Json(name = "artistName") val artistName: String,

    @Json(name = "collectionId") val collectionId: String,
    @Json(name = "collectionName") val collectionName: String
) {
    val artist: Artist?
    val collection: Collection

    init {
        artist = Artist(artistId, artistName)
        collection = Collection(collectionId, collectionName)
    }
}

//REF: CustomAdapter para clases complexas https://gist.github.com/alexforrester/5c96ace4227916fb456ff49a16ef025d
