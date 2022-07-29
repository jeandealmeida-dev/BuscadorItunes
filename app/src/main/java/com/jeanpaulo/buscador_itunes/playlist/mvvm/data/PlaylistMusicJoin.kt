package com.jeanpaulo.buscador_itunes.playlist.mvvm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.model.MusicEntity
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.entity.PlaylistEntity

@Entity(
    tableName = "play_music_join",
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["playlistId"],
            childColumns = ["playlistId"]
        ),
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = ["musicId"],
            childColumns = ["musicId"]
        )
    ]
)
data class PlaylistMusicJoin(
    @ColumnInfo(name = "playlistId") val playlistId: Long,
    @ColumnInfo(name = "musicId") val musicId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlistMusicJoinId") var id: Long = 0
)