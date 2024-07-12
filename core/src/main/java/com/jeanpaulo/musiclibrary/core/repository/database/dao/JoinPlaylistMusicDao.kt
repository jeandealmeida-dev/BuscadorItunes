package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.JoinPlaylistMusicEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.PlaylistEntity
import io.reactivex.rxjava3.core.Completable

@Dao
interface JoinPlaylistMusicDao {

    @Insert
    fun insert(joinPlaylistMusic: JoinPlaylistMusicEntity): Completable

//    @Query(
//        "SELECT * " +
//                "FROM playlist " +
//                "INNER JOIN ${JoinPlaylistMusicEntity.TABLE_NAME} ON playlist.playlistId=${JoinPlaylistMusicEntity.PLAYLIST_ID} " +
//                "WHERE ${JoinPlaylistMusicEntity.MUSIC_ID}=:musicId"
//    )
//    fun getPlaylistsWithThisMusic(musicId: Long): List<MusicEntity>

    @Query(
        "SELECT * " +
                "FROM ${MusicEntity.TABLE} " +
                "INNER JOIN ${JoinPlaylistMusicEntity.TABLE_NAME} ON ${MusicEntity.T_ID}=${JoinPlaylistMusicEntity.T_MUSIC_ID} " +
                "WHERE ${JoinPlaylistMusicEntity.PLAYLIST_ID}=:playlistId"
    )
    fun getMusicsFromPlaylist(playlistId: Long): List<MusicEntity>

    @Query(
        "SELECT * " +
                "FROM ${MusicEntity.TABLE} " +
                "INNER JOIN ${JoinPlaylistMusicEntity.TABLE_NAME} ON ${MusicEntity.T_ID}=${JoinPlaylistMusicEntity.T_MUSIC_ID}=:musicId " +
                "WHERE ${JoinPlaylistMusicEntity.T_PLAYLIST_ID}=:playlistId "
    )
    fun getMusicInPlaylist(musicId: Long, playlistId: Long): MusicEntity?

    @Query(
        "SELECT * " +
                "FROM ${JoinPlaylistMusicEntity.TABLE_NAME} " +
                "WHERE ${MusicEntity.ID}=:musicId AND ${PlaylistEntity.ID}=:playlistId"
    )
    fun getMusicOnPlaylist(musicId: Long, playlistId: Long): List<JoinPlaylistMusicEntity>

    @Query(
        "DELETE " +
                "FROM ${JoinPlaylistMusicEntity.TABLE_NAME} " +
                "WHERE ${MusicEntity.ID}=:musicId AND playlistId=:playlistId"
    )
    fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Completable
}