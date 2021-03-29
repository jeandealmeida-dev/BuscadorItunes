package com.jeanpaulo.buscador_itunes.datasource.remote.util

import com.jeanpaulo.buscador_itunes.datasource.remote.json_objects.MusicJson
import com.jeanpaulo.buscador_itunes.model.Music
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItunesResponse2(
    @Json(name = "results") val result: List<MusicJson>,
    @Json(name = "resultCount") val count: Int
)
