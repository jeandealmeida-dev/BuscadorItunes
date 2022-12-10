package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite

@Entity(
    tableName = "favorite",
    foreignKeys = [
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = ["musicId"],
            childColumns = ["musicId"]
        )
    ]
)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "favoriteId") var favoriteId: Long = 0,
    @ColumnInfo(name = "musicId") val musicId: Long
) {

    fun toModel() = Favorite(musicId)
}