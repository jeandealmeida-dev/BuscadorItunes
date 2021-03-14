package com.jeanpaulo.buscador_itunes.repository.remote.util

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItunesResponse2(
    @Json(name = "results") val result: com.jeanpaulo.buscador_itunes.model.Collection,
    @Json(name = "resultCount") val count: Int
)
