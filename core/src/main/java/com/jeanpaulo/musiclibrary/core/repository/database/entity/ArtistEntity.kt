package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Artist

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey @ColumnInfo(name = "artistId") val artistId: Long,
    @ColumnInfo(name = "artistName") val name: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "primaryGenreName") val primaryGenreName: String?
) {
    fun toModel(): Artist = Artist(artistId, name, country, primaryGenreName)
}