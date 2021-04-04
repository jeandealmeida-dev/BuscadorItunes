package com.jeanpaulo.buscador_itunes.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class _Collection(
    @Json(name = "collectionId") val id: Long?,
    @Json(name = "collectionName") val name: String?,
    @Json(name = "trackCount") val trackCount: Int?
) {
    val tracks: List<_Track> = listOf()
}