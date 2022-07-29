package com.jeanpaulo.buscador_itunes.playlist.mvvm.data.dao

import androidx.room.*
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.entity.PlaylistEntity

@Dao
interface PlaylistDao {

    //C

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity): kotlin.Long

    //R

    @Transaction
    @Query("SELECT * FROM Playlist WHERE title NOT LIKE :filter")
    suspend fun getPlaylistsFiltered(filter: String): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM Playlist")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Transaction
    @Query("SELECT * FROM Playlist WHERE title LIKE :playlistTitle")
    suspend fun getPlaylistByTitle(playlistTitle: String): List<PlaylistEntity?>?

    //U

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity): Int

    //D

    @Query("DELETE FROM Playlist WHERE playlistId = :playlistId")
    suspend fun deletePlaylistById(playlistId: kotlin.Long): Int

    @Query("DELETE FROM Playlist")
    suspend fun deletePlaylists()

}