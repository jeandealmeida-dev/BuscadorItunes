package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TrackResponse (
    @Json(name = "trackName") val name: String,
    @Json(name = "trackNumber") val trackNumber: Int,
    @Json(name = "previewUrl") val previewUrl: String
)