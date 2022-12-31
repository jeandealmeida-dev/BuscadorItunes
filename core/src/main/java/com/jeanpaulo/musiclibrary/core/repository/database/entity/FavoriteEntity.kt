package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite

@Entity(
    tableName = FavoriteEntity.TABLE,
    foreignKeys = [
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = [MusicEntity.ID],
            childColumns = [FavoriteEntity.MUSIC_ID]
        )
    ]
)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var favoriteId: Long = 0,
    @ColumnInfo(name = MUSIC_ID) val musicId: Long
) {
    companion object {
        const val TABLE = "favorite"

        const val ID = "id"
        const val MUSIC_ID = "musicId"

        const val T_ID = "${TABLE}.${ID}"
        const val T_MUSIC_ID = "${TABLE}.${MUSIC_ID}"
    }
}