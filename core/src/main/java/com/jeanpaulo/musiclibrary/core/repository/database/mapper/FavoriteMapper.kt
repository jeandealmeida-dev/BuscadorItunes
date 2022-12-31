package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity

fun Favorite.toEntity() : FavoriteEntity =
    FavoriteEntity(
        musicId = musicId
    )

fun FavoriteEntity.toModel() : Favorite =
    Favorite(
        musicId = musicId
    )