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
    @Json(name = "trackName") val trackName: String?,
    @Json(name = "artistId") val artistId: Long?,
    @Json(name = "artistName") val artistName: String,
    @Json(name = "collectionId") val collectionId: Long?,
    @Json(name = "collectionName") val collectionName: String?,
    @Json(name = "artworkUrl100") val artworkUrl: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "trackCount") val trackCount: Long?,
    @Json(name = "releaseDate") val releaseDate: Date?,
    @Json(name = "primaryGenreName") val primaryGenreName: String?,
    @Json(name = "isStreamable") val isStreamable: Boolean?,
    @Json(name = "trackTimeMillis") val trackTimeMillis: Long?,
    @Json(name = "previewUrl") val previewUrl: String?
) {
    fun toModel(): Music = Music(
        musicId = remoteId ?: NO_MUSIC_ID,
        trackName = trackName ?: NO_TRACK_NAME,
        artworkUrl = artworkUrl,
        releaseDate = releaseDate,
        streamable = isStreamable,
        trackTimeMillis = trackCount,
        previewUrl = previewUrl,
    ).also {
        it.musicArtist = Artist(
            artistId = artistId ?: NO_ID,
            name = artistName,
            country = country,
            primaryGenreName = primaryGenreName,
        )
        it.musicCollection = Collection(
            collectionId = collectionId ?: NO_ID,
            name = collectionName ?: NO_COLLECTION_NAME,
        )
    }

    companion object {
        const val NO_ID = -1L
        const val NO_MUSIC_ID = -1L

        const val NO_COLLECTION_NAME = "-"
        const val NO_TRACK_NAME = "-"
    }
}

