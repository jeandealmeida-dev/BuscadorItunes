package com.jeanpaulo.musiclibrary.core.repository.database.entity

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import org.junit.Assert.assertEquals
import org.junit.Test

class ArtistEntityTest {

    @Test
    fun `GIVEN an Artist model WHEN converting to entity THEN entity should have correct values`() {
        // GIVEN
        val artist = Artist(
            artistId = 1L,
            name = "Artist Name",
            country = "Country Name",
            primaryGenreName = "Genre Name"
        )

        // WHEN
        val artistEntity = ArtistEntity.from(artist)

        // THEN
        assertEquals(artist.artistId, artistEntity.artistId)
        assertEquals(artist.name, artistEntity.name)
        assertEquals(artist.country, artistEntity.country)
        assertEquals(artist.primaryGenreName, artistEntity.primaryGenreName)
    }

    @Test
    fun `GIVEN an ArtistEntity WHEN converting to model THEN model should have correct values`() {
        // GIVEN
        val artistEntity = ArtistEntity(
            artistId = 1L,
            name = "Artist Name",
            country = "Country Name",
            primaryGenreName = "Genre Name"
        )

        // WHEN
        val artistModel = artistEntity.toModel()

        // THEN
        assertEquals(artistEntity.artistId, artistModel.artistId)
        assertEquals(artistEntity.name, artistModel.name)
        assertEquals(artistEntity.country, artistModel.country)
        assertEquals(artistEntity.primaryGenreName, artistModel.primaryGenreName)
    }

    @Test
    fun `GIVEN an ArtistEntity WHEN created with default country and genre THEN defaults should be null`() {
        // GIVEN
        val artistId = 1L
        val name = "Artist Name"

        // WHEN
        val artistEntity = ArtistEntity(
            artistId = artistId,
            name = name
        )

        // THEN
        assertEquals(artistId, artistEntity.artistId)
        assertEquals(name, artistEntity.name)
        assertEquals(null, artistEntity.country)
        assertEquals(null, artistEntity.primaryGenreName)
    }

    @Test
    fun `GIVEN an ArtistEntity WHEN created with all values THEN values should be set correctly`() {
        // GIVEN
        val artistId = 1L
        val name = "Artist Name"
        val country = "Country Name"
        val genre = "Genre Name"

        // WHEN
        val artistEntity = ArtistEntity(
            artistId = artistId,
            name = name,
            country = country,
            primaryGenreName = genre
        )

        // THEN
        assertEquals(artistId, artistEntity.artistId)
        assertEquals(name, artistEntity.name)
        assertEquals(country, artistEntity.country)
        assertEquals(genre, artistEntity.primaryGenreName)
    }
}
