package com.jeanpaulo.buscador_itunes.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PlaylistWithMusics(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "musicId",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val musics: List<Music>
)