package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class MusicResponse(
    @Json(name = "trackId") val remoteId: Long?,
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
)