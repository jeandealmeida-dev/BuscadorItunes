package com.jeanpaulo.buscador_itunes.playlist.data.entity

import androidx.room.*
import com.jeanpaulo.buscador_itunes.music.domain.model.Playlist

@Entity(tableName = "playlist")
class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlistId") var playlistId: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?
) {
    fun toModel() = Playlist(playlistId, title, description)
}