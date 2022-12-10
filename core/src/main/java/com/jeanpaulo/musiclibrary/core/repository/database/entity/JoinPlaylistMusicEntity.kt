package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = JoinPlaylistMusicEntity.TABLE_NAME,
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
data class JoinPlaylistMusicEntity(
    @ColumnInfo(name = "playlistId") val playlistId: Long,
    @ColumnInfo(name = "musicId") val musicId: Long,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "playlistMusicJoinId") var id: Long = 0
){
    companion object {
        const val TABLE_NAME = "join_playlist_music"

        const val MUSIC_ID = "$TABLE_NAME.musicId"
        const val PLAYLIST_ID = "$TABLE_NAME.playlistId"
    }
}