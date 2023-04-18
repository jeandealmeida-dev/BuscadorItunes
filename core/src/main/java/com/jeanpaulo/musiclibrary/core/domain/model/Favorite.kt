package com.jeanpaulo.musiclibrary.core.domain.model

import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity

data class Favorite(
    val musicId: Long
) {
    lateinit var music: Music

    fun toEntity(): FavoriteEntity = FavoriteEntity(musicId = musicId)
}