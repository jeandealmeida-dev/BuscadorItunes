package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Artist

@Entity(tableName = ArtistEntity.TABLE)
data class ArtistEntity(
    @PrimaryKey @ColumnInfo(name = ARTIST_ID) val artistId: Long,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = COUNTRY) val country: String? = null,
    @ColumnInfo(name = PRIMARY_GENRE_NAME) val primaryGenreName: String? = null
) {

    fun toModel() = Artist(
        artistId = artistId,
        name = name,
        country = country,
        primaryGenreName = primaryGenreName,
    )

    companion object {
        const val TABLE = "artist"
        const val ARTIST_ID = "artist_id"

        const val NAME = "name"
        const val COUNTRY = "country"
        const val PRIMARY_GENRE_NAME = "primaryGenreName"

        fun from(artist: Artist) = ArtistEntity(
            artistId = artist.artistId,
            name = artist.name,
            country = artist.country,
            primaryGenreName = artist.primaryGenreName
        )
    }
}