package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ItunesCollectionResponse(
    @Json(name = "results") val result: List<WrapperType>,
    @Json(name = "resultCount") val count: Int
)
