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
            parentColumns = [PlaylistEntity.ID],
            childColumns = [JoinPlaylistMusicEntity.PLAYLIST_ID]
        ),
        ForeignKey(
            entity = MusicEntity::class,
            parentColumns = [MusicEntity.ID],
            childColumns = [JoinPlaylistMusicEntity.MUSIC_ID]
        )
    ]
)
data class JoinPlaylistMusicEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var id: Long = 0,
    @ColumnInfo(name = PLAYLIST_ID) val playlistId: Long,
    @ColumnInfo(name = MUSIC_ID) val musicId: Long
){
    companion object {
        const val TABLE_NAME = "join_playlist_music"

        const val ID = "id"
        const val MUSIC_ID = "musicId"
        const val PLAYLIST_ID = "playlistId"

        const val T_MUSIC_ID = "$TABLE_NAME.$MUSIC_ID"
        const val T_PLAYLIST_ID = "$TABLE_NAME.$PLAYLIST_ID"
    }
}