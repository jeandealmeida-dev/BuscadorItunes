package com.jeanpaulo.buscador_itunes.model

import com.jeanpaulo.buscador_itunes.datasource.local.entity.ArtistEntity
import com.jeanpaulo.buscador_itunes.datasource.local.entity.CollectionEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


class Collection(
    val collectionId: Long?,
    val name: String?
) {
    fun toEntity(): CollectionEntity = CollectionEntity(collectionId!!, name)
}