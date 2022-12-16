package com.jeanpaulo.musiclibrary.favorite.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

interface FavoriteInteractor {
    fun removeMusicFromFavorites(trackId: Long): Completable
    fun getFavoriteMusics(): Flowable<List<Favorite>>
}

class FavoriteInteractorImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
//    private val musicRepository: MusicRepository
) : FavoriteInteractor {

//    override fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean> =
//        favoriteRepository.isOnFavoritedPlaylist(dsTrackid)

//    override suspend fun saveMusicInFavorites(music: Music): Result<Long> =
//        favoriteRepository.saveMusicInFavorites(music)

    override fun getFavoriteMusics(): Flowable<List<Favorite>> {
        return favoriteRepository.getFavorites()
            .map { favoriteList ->
                favoriteList.map { favorite ->
//                    musicRepository.getSingleMusic(favorite.musicId)
//                        .subscribe { music ->
//                            favorite.music = music
//                        }
                }
                return@map favoriteList
            }


//            .let { result ->
//            val musicList = mutableListOf<Music>()
//            result.data.map { favorite ->
//                musicRepository.getMusic(favorite.musicId)
//                musicList.add()
//            }
//        }
    }

    override fun removeMusicFromFavorites(musicId: Long): Completable =
        favoriteRepository.removeFromFavorites(musicId)

}