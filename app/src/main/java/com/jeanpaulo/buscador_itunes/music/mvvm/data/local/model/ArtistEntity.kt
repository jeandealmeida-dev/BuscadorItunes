package com.jeanpaulo.buscador_itunes.music.mvvm.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Artist

@Entity(tableName = "artist")
data class ArtistEntity(
    @PrimaryKey @ColumnInfo(name = "artistId") val artistId: Long,
    @ColumnInfo(name = "artistName") val name: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "primaryGenreName") val primaryGenreName: String?
) {
    fun toModel(): Artist = Artist(artistId, name, country, primaryGenreName)
}