package com.jeanpaulo.musiclibrary.favorite.data

import com.jeanpaulo.musiclibrary.core.repository.database.dao.FavoriteDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


interface FavoriteRepository {

    fun isFavorited(dsTrackid: Long): Single<Boolean>

    fun addInFavorites(musicId: Long): Single<Long>

    fun getFavorites(): Flowable<List<Favorite>>

    fun removeFromFavorites(musicId: Long): Completable
}

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoriteRepository {

    override fun addInFavorites(musicId: Long): Single<Long> =
        favoriteDao.insert(FavoriteEntity(musicId = musicId))


    override fun isFavorited(dsTrackid: Long): Single<Boolean> =
        favoriteDao.isFavorite(dsTrackid).map { it > 0 }


    override fun getFavorites(): Flowable<List<Favorite>> =
        favoriteDao.getFavorites().map { list -> list.map { it.toModel() } }

    override fun removeFromFavorites(musicId: Long): Completable =
        favoriteDao.removeMusicFromFavorite(musicId)
}