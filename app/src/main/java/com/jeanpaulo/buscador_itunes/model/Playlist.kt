package com.jeanpaulo.buscador_itunes.model

import androidx.room.*
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Playlist")
class Playlist(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @PrimaryKey val playlistId: String = UUID.randomUUID().toString()
) {
    val isEmpty
        get() = title.isEmpty()
}