package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.repository.database.entity.CollectionEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Collection

fun Collection.toEntity(): CollectionEntity =
    CollectionEntity(
        id = id,
        name = name
    )

fun CollectionEntity.toModel() = Collection(
        id = id,
        name = name
    )