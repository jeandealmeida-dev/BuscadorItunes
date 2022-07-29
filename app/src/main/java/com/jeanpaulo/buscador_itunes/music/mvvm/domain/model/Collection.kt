package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.model.CollectionEntity

class Collection(
    val collectionId: Long?,
    val name: String?
) {
    fun toEntity(): CollectionEntity = CollectionEntity(collectionId!!, name)
}