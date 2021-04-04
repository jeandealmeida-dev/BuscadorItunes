package com.jeanpaulo.buscador_itunes.datasource.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist

data class PlaylistWithMusics(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "musicId",
        associateBy = Junction(PlaylistMusicCrossRef::class)
    )
    val musics: List<MusicEntity>
)