package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class MusicResponse(
    @Json(name = "trackId") val remoteId: Long?,
    @Json(name = "trackName") val trackName: String? = null,
    @Json(name = "artistId") val artistId: Long? = null,
    @Json(name = "artistName") val artistName: String? = null,
    @Json(name = "collectionId") val collectionId: Long? = null,
    @Json(name = "collectionName") val collectionName: String? = null,
    @Json(name = "artworkUrl100") val artworkUrl: String? = null,
    @Json(name = "country") val country: String? = null,
    @Json(name = "trackCount") val trackCount: Long? = null,
    @Json(name = "releaseDate") val releaseDate: Date? = null,
    @Json(name = "primaryGenreName") val primaryGenreName: String? = null,
    @Json(name = "isStreamable") val isStreamable: Boolean? = null,
    @Json(name = "trackTimeMillis") val trackTimeMillis: Long? = null,
    @Json(name = "previewUrl") val previewUrl: String? = null
) {
    fun toModel(): Music = Music(
        musicId = remoteId ?: NO_MUSIC_ID,
        trackName = trackName ?: EMPTY,
        artworkUrl = artworkUrl,
        releaseDate = releaseDate,
        streamable = isStreamable,
        trackTimeMillis = trackCount,
        previewUrl = previewUrl,
    ).also {
        it.musicArtist = Artist(
            artistId = artistId ?: NO_ID,
            name = artistName ?: EMPTY,
            country = country,
            primaryGenreName = primaryGenreName,
        )
        it.musicCollection = Collection(
            collectionId = collectionId ?: NO_ID,
            name = collectionName ?: EMPTY,
        )
    }

    companion object {
        const val NO_ID = -1L
        const val NO_MUSIC_ID = -1L

        const val EMPTY = ""
    }
}

