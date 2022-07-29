package com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.response

import com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.model.MusicJson
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchMusicResponse(
    @Json(name = "results") val result: List<MusicJson>,
    @Json(name = "resultCount") val count: Int
)
