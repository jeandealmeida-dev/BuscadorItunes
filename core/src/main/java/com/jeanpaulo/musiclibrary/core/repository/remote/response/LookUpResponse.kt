package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LookUpResponse(
    @Json(name = "results") val result: List<MusicResponse>,
    @Json(name = "resultCount") val count: Int
)
