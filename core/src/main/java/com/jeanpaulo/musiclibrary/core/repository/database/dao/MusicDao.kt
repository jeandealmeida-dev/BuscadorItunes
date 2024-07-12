package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface MusicDao {

    // Insert

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMusic(music: MusicEntity): Single<Long>

    // Select

    @Query(
        "SELECT * " +
                "FROM ${MusicEntity.TABLE} " +
                "WHERE ${MusicEntity.MUSIC_ID} = :remoteId"
    )
    fun getMusicByRemoteId(remoteId: Long): Single<MusicEntity>

    @Query(
        "SELECT * " +
                "FROM ${MusicEntity.TABLE} " +
                "WHERE ${MusicEntity.ID} = :id"
    )
    fun getMusicById(id: Long): Single<MusicEntity>

    @Query(
        "SELECT * " +
                "FROM ${MusicEntity.TABLE}"
    )
    fun getMusics(): List<MusicEntity>

    // Update

    @Update
    fun updateMusic(music: MusicEntity): Completable

    // Delete

    @Query(
        "DELETE " +
                "FROM ${MusicEntity.TABLE} " +
                "WHERE ${MusicEntity.ID} = :id"
    )
    fun deleteMusicById(id: Long): Completable

    @Query(
        "DELETE " +
                "FROM ${MusicEntity.TABLE}"
    )
    fun deleteMusics(): Completable
}