package com.jeanpaulo.musiclibrary.core.repository.database.entity

import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import org.junit.Assert.assertEquals
import org.junit.Test

class PlaylistEntityTest {

    @Test
    fun `GIVEN a Playlist model WHEN converting to entity THEN entity should have correct values`() {
        // GIVEN
        val playlist = Playlist(
            playlistId = 1L,
            title = "My Playlist",
            description = "A playlist for testing"
        )

        // WHEN
        val playlistEntity = PlaylistEntity.from(playlist)

        // THEN
        assertEquals(playlist.playlistId, playlistEntity.playlistId)
        assertEquals(playlist.title, playlistEntity.title)
        assertEquals(playlist.description, playlistEntity.description)
    }

    @Test
    fun `GIVEN a PlaylistEntity WHEN converting to model THEN model should have correct values`() {
        // GIVEN
        val playlistEntity = PlaylistEntity(
            playlistId = 1L,
            title = "My Playlist",
            description = "A playlist for testing"
        )

        // WHEN
        val playlistModel = playlistEntity.toModel()

        // THEN
        assertEquals(playlistEntity.playlistId, playlistModel.playlistId)
        assertEquals(playlistEntity.title, playlistModel.title)
        assertEquals(playlistEntity.description, playlistModel.description)
    }

    @Test
    fun `GIVEN a PlaylistEntity WHEN created with default constructor THEN id should be zero and other properties should be set`() {
        // GIVEN
        val title = "My Playlist"
        val description = "A playlist for testing"

        // WHEN
        val playlistEntity = PlaylistEntity(
            title = title,
            description = description
        )

        // THEN
        assertEquals(0L, playlistEntity.playlistId)
        assertEquals(title, playlistEntity.title)
        assertEquals(description, playlistEntity.description)
    }
}
