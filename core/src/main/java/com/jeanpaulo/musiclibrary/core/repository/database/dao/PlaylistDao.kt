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
    @Query(
        "SELECT * " +
                "FROM ${PlaylistEntity.TABLE} " +
                "WHERE ${PlaylistEntity.TITTLE} NOT LIKE :filter"
    )
    fun getPlaylistsFiltered(filter: String): Single<List<PlaylistEntity>>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM ${PlaylistEntity.TABLE} "
    )
    fun getPlaylists(): Flowable<List<PlaylistEntity>>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM ${PlaylistEntity.TABLE} " +
                "WHERE ${PlaylistEntity.ID} = :playlistId"
    )
    fun getPlaylistById(playlistId: Long): Single<PlaylistEntity>

    @Transaction
    @Query(
        "SELECT * " +
                "FROM ${PlaylistEntity.TABLE} " +
                "WHERE ${PlaylistEntity.TITTLE} LIKE :playlistTitle"
    )
    fun getPlaylistByTitle(playlistTitle: String): Single<List<PlaylistEntity>>

    @Update
    fun updatePlaylist(playlist: PlaylistEntity): Int

    @Query(
        "DELETE " +
                "FROM ${PlaylistEntity.TABLE} " +
                "WHERE ${PlaylistEntity.ID} = :playlistId"
    )
    fun deletePlaylistById(playlistId: Long): Completable

    @Query(
        "DELETE " +
                "FROM ${PlaylistEntity.TABLE} "
    )
    fun deletePlaylists()

}