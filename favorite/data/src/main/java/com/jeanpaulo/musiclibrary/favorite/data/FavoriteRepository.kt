package com.jeanpaulo.musiclibrary.favorite.data

import androidx.room.rxjava3.EmptyResultSetException
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.repository.database.dao.FavoriteDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.FavoriteEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject


interface FavoriteRepository {
    fun isFavorite(remoteId: Long): Single<Boolean>
    fun getAll(): Flowable<List<Favorite>>
    fun save(musicId: Long): Completable
    fun remove(musicId: Long): Completable
    fun getCount(): Single<Int>
}

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoriteRepository {

    override fun save(musicId: Long): Completable =
        favoriteDao.insert(FavoriteEntity(musicId = musicId))

    override fun isFavorite(remoteId: Long): Single<Boolean> {
        return favoriteDao.isFavorite(remoteId)
            .map { it > 0 }
            .onErrorResumeNext {
                when (it) {
                    is EmptyResultSetException -> {
                        Single.error(EmptyResultException())
                    }

                    else -> {
                        Single.error(it)
                    }
                }
            }
    }


    override fun getAll(): Flowable<List<Favorite>> =
        favoriteDao.getFavorites().map { list ->
            list.map { it.toModel() }
        }

    override fun getCount(): Single<Int> =
        favoriteDao.getCount()

    override fun remove(remoteId: Long): Completable =
        favoriteDao.removeMusicFromFavorite(remoteId)
}