package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanpaulo.musiclibrary.core.domain.model.Artist

@Entity(tableName = ArtistEntity.TABLE)
data class ArtistEntity(
    @PrimaryKey @ColumnInfo(name = ID) val artistId: Long,
    @ColumnInfo(name = NAME) val name: String,
    @ColumnInfo(name = COUNTRY) val country: String?,
    @ColumnInfo(name = PRIMARY_GENRE_NAME) val primaryGenreName: String?
) {
    companion object {
        const val TABLE = "artist"
        const val ID = "id"
        const val NAME = "name"
        const val COUNTRY = "country"
        const val PRIMARY_GENRE_NAME = "primaryGenreName"
    }
}