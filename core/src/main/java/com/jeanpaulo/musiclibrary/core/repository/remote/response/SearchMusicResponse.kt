package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMusicResponse(
    @Json(name = "results") val result: List<MusicResponse>,
    @Json(name = "resultCount") val count: Int
)
