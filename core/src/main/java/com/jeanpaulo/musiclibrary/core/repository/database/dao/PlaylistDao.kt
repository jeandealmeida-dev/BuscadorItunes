package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.PlaylistEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: PlaylistEntity): Single<Long>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE title NOT LIKE :filter")
    fun getPlaylistsFiltered(filter: String): Single<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylists(): Flowable<List<PlaylistEntity>>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId = :playlistId")
    fun getPlaylistById(playlistId: Long): Single<PlaylistEntity?>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE title LIKE :playlistTitle")
    fun getPlaylistByTitle(playlistTitle: String): Single<List<PlaylistEntity>>

    @Update
    fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query("DELETE FROM Playlist WHERE playlistId = :playlistId")
    fun deletePlaylistById(playlistId: kotlin.Long): Completable

    @Query("DELETE FROM Playlist")
    fun deletePlaylists()

}