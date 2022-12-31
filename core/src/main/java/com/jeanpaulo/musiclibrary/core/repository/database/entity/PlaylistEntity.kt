package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist

@Entity(tableName = PlaylistEntity.TABLE)
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var playlistId: Long = 0,
    @ColumnInfo(name = TITTLE) val title: String,
    @ColumnInfo(name = DESCRIPTION) val description: String?
) {
    companion object {
        const val TABLE = "playlist"

        const val ID = "id"
        const val TITTLE = "tittle"
        const val DESCRIPTION = "description"

        const val T_ID = "$TABLE.$ID"
    }
}