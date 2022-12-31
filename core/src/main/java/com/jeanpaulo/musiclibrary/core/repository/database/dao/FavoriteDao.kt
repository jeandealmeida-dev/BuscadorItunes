package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntity): Completable

    @Transaction
    @Query(
        value = "SELECT ${FavoriteEntity.T_ID} " +
                "FROM ${FavoriteEntity.TABLE}  " +
                "INNER JOIN ${MusicEntity.TABLE} ON ${MusicEntity.T_ID}=${FavoriteEntity.T_MUSIC_ID} " +
                "WHERE ${MusicEntity.T_REMOTE_ID} = :remoteId"
    )
    fun isFavorite(remoteId: Long): Single<Int> // 2 options: Maybe or Single ->  if I use Single Room will throw EmptyResultSetException.

    @Query("DELETE " +
            "FROM ${FavoriteEntity.TABLE} " +
            "WHERE ${FavoriteEntity.MUSIC_ID} IN " +
            "( " +
                "SELECT ${MusicEntity.T_ID} " +
                "FROM ${MusicEntity.TABLE} " +
                "INNER JOIN ${FavoriteEntity.TABLE} ON ${MusicEntity.T_ID}=${FavoriteEntity.T_MUSIC_ID} " +
                "WHERE ${MusicEntity.REMOTE_ID} = :remoteId " +
            ")"
    )
    fun removeMusicFromFavorite(remoteId: Long): Completable

    @Transaction
    @Query("SELECT * " +
            "FROM ${FavoriteEntity.TABLE} ")
    fun getFavorites(): Flowable<List<FavoriteEntity>>

}