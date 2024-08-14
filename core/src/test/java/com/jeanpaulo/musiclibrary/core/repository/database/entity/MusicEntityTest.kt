package com.jeanpaulo.musiclibrary.core.repository.database.entity

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import org.junit.Assert.*
import org.junit.Test
import java.util.Date

class MusicEntityTest {

    @Test
    fun `GIVEN a Music model WHEN converting to entity THEN entity should have correct values`() {
        // GIVEN
        val artist = Artist(artistId = 5678L, "Artist 1")
        val collection = Collection(collectionId = 1234L,  "Collection 1")
        val music = Music(
            id = 1L,
            musicId = 4321L,
            trackName = "Test Track",
            artworkUrl = "http://testurl.com/artwork.jpg",
            releaseDate = Date(),
            streamable = true,
            trackTimeMillis = 300000L,
            previewUrl = "http://testurl.com/preview.mp3"
        ).apply {
            musicArtist = artist
            musicCollection = collection
        }

        // WHEN
        val musicEntity = MusicEntity.fromModel(music)

        // THEN
        assertEquals(music.id, musicEntity.id)
        assertEquals(music.musicId, musicEntity.musicId)
        assertEquals(music.trackName, musicEntity.name)
        assertEquals(music.artworkUrl, musicEntity.artworkUrl)
        assertEquals(music.releaseDate, musicEntity.releaseDate)
        assertEquals(music.streamable, musicEntity.isStreamable)
        assertEquals(music.trackTimeMillis, musicEntity.trackTimeMillis)
        assertEquals(music.previewUrl, musicEntity.previewUrl)
        assertEquals(music.musicArtist?.artistId, musicEntity.artistId)
        assertEquals(music.musicCollection?.collectionId, musicEntity.collectionId)
    }

    @Test
    fun `GIVEN a MusicEntity WHEN converting to model THEN model should have correct values`() {
        // GIVEN
        val releaseDate = Date()
        val musicEntity = MusicEntity(
            id = 1L,
            musicId = 4321L,
            name = "Test Track",
            artworkUrl = "http://testurl.com/artwork.jpg",
            releaseDate = releaseDate,
            isStreamable = true,
            trackTimeMillis = 300000L,
            previewUrl = "http://testurl.com/preview.mp3",
            artistId = 5678L,
            collectionId = 1234L
        ).apply {
            artist = ArtistEntity(artistId = 5678L, "Artist 1")
            collection = CollectionEntity(collectionId = 1234L, "Collection 1")
        }

        // WHEN
        val musicModel = musicEntity.toModel()

        // THEN
        assertEquals(musicEntity.id, musicModel.id)
        assertEquals(musicEntity.musicId, musicModel.musicId)
        assertEquals(musicEntity.name, musicModel.trackName)
        assertEquals(musicEntity.artworkUrl, musicModel.artworkUrl)
        assertEquals(musicEntity.releaseDate, musicModel.releaseDate)
        assertEquals(musicEntity.isStreamable, musicModel.streamable)
        assertEquals(musicEntity.trackTimeMillis, musicModel.trackTimeMillis)
        assertEquals(musicEntity.previewUrl, musicModel.previewUrl)
        assertNotNull(musicModel.musicArtist)
        assertNotNull(musicModel.musicCollection)
    }

    @Test
    fun `GIVEN a MusicEntity WHEN created with default constructor THEN id should be zero and other properties should be set`() {
        // GIVEN
        val musicId = 4321L
        val name = "Test Track"
        val artworkUrl = "http://testurl.com/artwork.jpg"
        val releaseDate = Date()
        val isStreamable = true
        val trackTimeMillis = 300000L
        val previewUrl = "http://testurl.com/preview.mp3"
        val artistId = 5678L
        val collectionId = 1234L

        // WHEN
        val musicEntity = MusicEntity(
            musicId = musicId,
            name = name,
            artworkUrl = artworkUrl,
            releaseDate = releaseDate,
            isStreamable = isStreamable,
            trackTimeMillis = trackTimeMillis,
            previewUrl = previewUrl,
            artistId = artistId,
            collectionId = collectionId
        )

        // THEN
        assertEquals(0L, musicEntity.id)
        assertEquals(musicId, musicEntity.musicId)
        assertEquals(name, musicEntity.name)
        assertEquals(artworkUrl, musicEntity.artworkUrl)
        assertEquals(releaseDate, musicEntity.releaseDate)
        assertEquals(isStreamable, musicEntity.isStreamable)
        assertEquals(trackTimeMillis, musicEntity.trackTimeMillis)
        assertEquals(previewUrl, musicEntity.previewUrl)
        assertEquals(artistId, musicEntity.artistId)
        assertEquals(collectionId, musicEntity.collectionId)
    }
}
