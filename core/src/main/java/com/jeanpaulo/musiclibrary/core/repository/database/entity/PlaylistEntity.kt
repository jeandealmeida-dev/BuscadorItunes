package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist

@Entity(tableName = "playlist")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlistId") var playlistId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?
) {
    fun toModel() = Playlist(playlistId, title, description)

    fun setId(id: Long){
        this.playlistId = id
    }
}