package com.jeanpaulo.buscador_itunes.music.domain.model

import com.jeanpaulo.buscador_itunes.music.data.local.model.CollectionEntity

class Collection(
    val collectionId: Long?,
    val name: String?
) {
    fun toEntity(): CollectionEntity = CollectionEntity(collectionId!!, name)
}