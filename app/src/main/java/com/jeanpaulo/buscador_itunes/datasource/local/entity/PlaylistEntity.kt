package com.jeanpaulo.buscador_itunes.datasource.local.entity

import androidx.room.*
import com.jeanpaulo.buscador_itunes.model.Playlist
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Playlist")
class PlaylistEntity(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @PrimaryKey val playlistId: String = UUID.randomUUID().toString()
) {
    fun toModel(): Playlist {
        return Playlist().let {
            it.playlistId = playlistId
            it.title = title
            it.description = description
            it
        }
    }
}