package com.jeanpaulo.musiclibrary.core.repository.database.dao

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntity): Single<Long>

    @Transaction
    @Query("SELECT favoriteId FROM Favorite WHERE musicId = :musicId")
    fun isFavorite(musicId: Long): Single<Long>

    @Query("DELETE FROM Favorite WHERE musicId = :musicId")
    fun removeMusicFromFavorite(musicId: Long) : Completable

    @Transaction
    @Query("SELECT * FROM Favorite")
    fun getFavorites(): Flowable<List<FavoriteEntity>>


}