package com.jeanpaulo.buscador_itunes.playlist.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeanpaulo.buscador_itunes.music.data.local.model.MusicEntity
import com.jeanpaulo.buscador_itunes.playlist.data.PlaylistMusicJoin

@Dao
interface PlaylistWithMusicDao {

    //C

    @Insert
    suspend fun insert(playlistMusicJoin: PlaylistMusicJoin): kotlin.Long

    //R

    @Query("SELECT * FROM playlist INNER JOIN play_music_join ON playlist.playlistId=play_music_join.playlistId WHERE play_music_join.musicId=:musicId")
    suspend fun getPlaylistsWithThisMusic(musicId: Long): List<MusicEntity>

    @Query("SELECT * FROM music INNER JOIN play_music_join ON music.musicId=play_music_join.musicId WHERE play_music_join.playlistId=:playlistId")
    suspend fun getMusicsFromPlaylist(playlistId: Long): List<MusicEntity>

    @Query("SELECT * FROM music INNER JOIN play_music_join ON music.musicId =play_music_join.musicId WHERE play_music_join.playlistId=:playlistId AND music.musicId=:musicId ")
    suspend fun getMusicInPlaylist(musicId: Long, playlistId: kotlin.Long): MusicEntity?

    @Query("SELECT * FROM play_music_join WHERE musicId=:musicId AND playlistId=:playlistId")
    suspend fun getMusicOnPlaylist(musicId: Long, playlistId: Long): List<PlaylistMusicJoin>

    //D

    @Query("DELETE FROM play_music_join WHERE musicId=:musicId AND playlistId=:playlistId")
    suspend fun removeMusicFromPlaylist(musicId: kotlin.Long, playlistId: kotlin.Long)
}