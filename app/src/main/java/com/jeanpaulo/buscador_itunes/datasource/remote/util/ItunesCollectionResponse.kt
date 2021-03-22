package com.jeanpaulo.buscador_itunes.datasource.remote.util

import com.jeanpaulo.buscador_itunes.model.util.WrapperType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItunesCollectionResponse(
    @Json(name = "results") val result: List<WrapperType>,
    @Json(name = "resultCount") val count: Int
)
