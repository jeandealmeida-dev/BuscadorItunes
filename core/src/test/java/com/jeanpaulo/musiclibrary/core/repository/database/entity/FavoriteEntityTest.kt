package com.jeanpaulo.musiclibrary.core.repository.database.entity

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteEntityTest {

    @Test
    fun `GIVEN a Favorite model WHEN converting to entity THEN entity should have correct values`() {
        // GIVEN
        val favorite = Favorite(musicId = 1234L)

        // WHEN
        val favoriteEntity = FavoriteEntity.fromModel(favorite)

        // THEN
        assertEquals(favorite.musicId, favoriteEntity.musicId)
    }

    @Test
    fun `GIVEN a FavoriteEntity WHEN converting to model THEN model should have correct values`() {
        // GIVEN
        val favoriteEntity = FavoriteEntity(id = 1L, musicId = 1234L)

        // WHEN
        val favoriteModel = favoriteEntity.toModel()

        // THEN
        assertEquals(favoriteEntity.musicId, favoriteModel.musicId)
    }

    @Test
    fun `GIVEN a FavoriteEntity WHEN created with default constructor THEN id should be zero and musicId should be set`() {
        // GIVEN
        val musicId = 1234L

        // WHEN
        val favoriteEntity = FavoriteEntity(musicId = musicId)

        // THEN
        assertEquals(0L, favoriteEntity.id)
        assertEquals(musicId, favoriteEntity.musicId)
    }
}
