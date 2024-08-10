package com.jeanpaulo.musiclibrary.core.repository.database.entity

import org.junit.Assert.assertEquals
import org.junit.Test

class JoinPlaylistMusicEntityTest {

    @Test
    fun `GIVEN playlistId and musicId WHEN creating JoinPlaylistMusicEntity THEN entity should have correct values`() {
        // GIVEN
        val playlistId = 1L
        val musicId = 2L

        // WHEN
        val joinPlaylistMusicEntity = JoinPlaylistMusicEntity(
            playlistId = playlistId,
            musicId = musicId
        )

        // THEN
        assertEquals(playlistId, joinPlaylistMusicEntity.playlistId)
        assertEquals(musicId, joinPlaylistMusicEntity.musicId)
        assertEquals(0L, joinPlaylistMusicEntity.id) // Default value
    }

    @Test
    fun `GIVEN a JoinPlaylistMusicEntity WHEN modified THEN changes should be reflected correctly`() {
        // GIVEN
        val joinPlaylistMusicEntity = JoinPlaylistMusicEntity(
            playlistId = 1L,
            musicId = 2L
        )

        // WHEN
        joinPlaylistMusicEntity.id = 3L

        // THEN
        assertEquals(3L, joinPlaylistMusicEntity.id)
    }

    @Test
    fun `GIVEN JoinPlaylistMusicEntity WHEN created with default constructor THEN id should be zero and other properties should be set`() {
        // GIVEN
        val playlistId = 1L
        val musicId = 2L

        // WHEN
        val joinPlaylistMusicEntity = JoinPlaylistMusicEntity(
            playlistId = playlistId,
            musicId = musicId
        )

        // THEN
        assertEquals(0L, joinPlaylistMusicEntity.id)
        assertEquals(playlistId, joinPlaylistMusicEntity.playlistId)
        assertEquals(musicId, joinPlaylistMusicEntity.musicId)
    }
}
