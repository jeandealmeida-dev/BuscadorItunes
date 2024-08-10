package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist

@Entity(tableName = PlaylistEntity.TABLE)
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var playlistId: Long = 0,
    @ColumnInfo(name = TITTLE) val title: String,
    @ColumnInfo(name = DESCRIPTION) val description: String?
) {

    fun toModel(): Playlist =
        Playlist(
            playlistId = playlistId,
            title = title,
            description = description
        )

    companion object {
        const val TABLE = "playlist"

        const val ID = "id"
        const val TITTLE = "tittle"
        const val DESCRIPTION = "description"

        fun from(playlist: Playlist) = PlaylistEntity(
            playlistId = playlist.playlistId,
            title = playlist.title,
            description = playlist.description
        )
    }
}