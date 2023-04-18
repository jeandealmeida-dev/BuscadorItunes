package com.jeanpaulo.musiclibrary.favorite.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.music.data.MusicRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface FavoriteInteractor {
    fun isFavorite(dsTrackid: Long): Single<Boolean>
    fun getFavoriteMusics(): Flowable<List<Favorite>>

    fun removeFromFavorites(trackId: Long): Completable
    fun saveInFavorite(dsTrackid: Long): Completable
}

class FavoriteInteractorImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val musicRepository: MusicRepository
) : FavoriteInteractor {

    override fun isFavorite(dsTrackid: Long): Single<Boolean> =
        favoriteRepository.isFavorite(dsTrackid)

    override fun saveInFavorite(dsTrackid: Long): Completable =
        favoriteRepository.save(dsTrackid)

    override fun removeFromFavorites(musicId: Long): Completable =
        favoriteRepository.remove(musicId)


    override fun getFavoriteMusics(): Flowable<List<Favorite>> {
        return favoriteRepository.getAll()
            .map { favoriteList ->
                favoriteList.map { favorite ->
                    favorite.music = musicRepository.get(favorite.musicId).blockingGet()
                }
                return@map favoriteList
            }
    }
}