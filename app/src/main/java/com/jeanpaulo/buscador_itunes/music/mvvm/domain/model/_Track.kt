package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class _Track (
    @Json(name = "trackName") val name: String,
    @Json(name = "trackNumber") val trackNumber: Int,
    @Json(name = "previewUrl") val previewUrl: String
)