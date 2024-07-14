package com.jeanpaulo.musiclibrary.core

import com.jeanpaulo.musiclibrary.core.music_player.model.MPPlaylist
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import junit.framework.Assert.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MPPlaylistTest {

    private fun createPlaylist(songs: List<MPSong> = emptyList()) =
        MPPlaylist(songs.toMutableList())

    @Test
    fun isEmpty_emptyPlaylist_returnsTrue() {
        val playlist = createPlaylist()
        assertFalse(playlist.hasNext())
    }

    @Test
    fun isEmpty_nonEmptyPlaylist_returnsFalse() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(
            listOf(
                song
            )
        )
        val playedSong = playlist.play()
        assertNotNull(playedSong)
        assertEquals(playedSong?.id, song.id)
        assertFalse(playlist.hasNext())
    }

    @Test
    fun previous_emptyPlaylist_returnsFalse() {
        val playlist = createPlaylist()
        assertFalse(playlist.previous())
    }

    @Test
    fun previous_lastSongPlaylist_returnsTrue() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(
            listOf(
                song
            )
        )
        assertEquals(playlist.play()?.id, song.id)
        assertFalse(playlist.hasPrevious())
        assertFalse(playlist.hasNext())
    }

    @Test
    fun previous_moreSongsPlaylist_returnsTrue() {
        val song1 = MPSong(1, "Song Name", "Artist Name", null, null)
        val song2 = MPSong(2, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(
            listOf(
                song1, song2
            )
        )
        assertEquals(playlist.play()?.id, song1.id)

        assertTrue(playlist.next())
        assertEquals(playlist.play()?.id, song2.id)

        assertFalse(playlist.next())
        assertEquals(playlist.play()?.id, song2.id)

        assertTrue(playlist.previous())
        assertEquals(playlist.play()?.id, song1.id)

        assertFalse(playlist.previous())
    }

    @Test
    fun next_emptyPlaylist_returnsFalse() {
        val playlist = createPlaylist()
        assertFalse(playlist.next())
    }

    @Test
    fun next_lastSongPlaylist_returnsTrue() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(
            listOf(
                song
            )
        )
        assertEquals(playlist.play()?.id, song.id)
        assertFalse(playlist.next())

        // Check
        assertEquals(playlist.play()?.id, song.id)
        assertFalse(playlist.hasNext())
    }

    @Test
    fun next_moreSongsPlaylist_returnsTrue() {
        val song1 = MPSong(1, "Song Name", "Artist Name", null, null)
        val song2 = MPSong(2, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(
            listOf(
                song1, song2
            )
        )

        // Initial State
        assertEquals(playlist.play()?.id, song1.id)

        // First next Call
        assertTrue(playlist.next())
        assertEquals(playlist.play()?.id, song2.id)
        assertFalse(playlist.hasNext())

        // Second next call
        assertFalse(playlist.next())
        assertEquals(playlist.play()?.id, song2.id)
        assertFalse(playlist.hasNext())
    }

    @Test
    fun play_emptyPlaylistAndPrevious_returnsNull() {
        val playlist = createPlaylist()
        assertEquals(null, playlist.play())
    }

    @Test
    fun play_nonEmptyPlaylist_returnsFirstSong() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist(listOf(song))
        assertEquals(song, playlist.play())
    }

    @Test
    fun play_emptyPlaylistNonEmptyPrevious_returnsFirstPreviousSong() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist()
        playlist.add(song)
        playlist.next()
        assertEquals(song, playlist.play())
    }

    // New test cases:

    @Test
    fun add_songToEmptyPlaylist_increasesSize() {
        val song = MPSong(1, "Song Name", "Artist Name", null, null)
        val playlist = createPlaylist()

        playlist.add(song)

        assertEquals(1, playlist.songs.size)
        assertTrue(playlist.songs.contains(song))
    }

    @Test
    fun add_multipleSongs_addsInCorrectOrder() {
        val song1 = MPSong(1, "Song 1", "Artist 1", null, null)
        val song2 = MPSong(2, "Song 2", "Artist 2", null, null)
        val song3 = MPSong(3, "Song 3", "Artist 3", null, null)

        val playlist = createPlaylist()

        playlist.add(song1)
        playlist.add(song2)
        playlist.add(song3)

        assertEquals(3, playlist.songs.size)
        assertEquals(listOf(song1, song2, song3), playlist.songs)
    }
}

