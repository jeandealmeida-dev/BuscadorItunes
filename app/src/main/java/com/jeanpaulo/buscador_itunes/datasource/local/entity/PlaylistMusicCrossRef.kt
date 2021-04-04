package com.jeanpaulo.buscador_itunes.datasource.local.entity

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "musicId"])
data class PlaylistMusicCrossRef(
    val playlistId: String,
    val musicId: String
)