package com.jeanpaulo.buscador_itunes.model

import androidx.room.Entity

@Entity(primaryKeys = ["playlistId", "musicId"])
data class PlaylistMusicCrossRef(
    val playlistId: String,
    val musicId: String
)