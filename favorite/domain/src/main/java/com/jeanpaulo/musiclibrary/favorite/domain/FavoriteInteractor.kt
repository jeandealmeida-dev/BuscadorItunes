package com.jeanpaulo.musiclibrary.favorite.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.CollectionEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.music.data.MusicRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface FavoriteInteractor {
    fun isFavorite(dsTrackid: Long): Single<Boolean>
    fun getFavoriteMusics(): Flowable<List<Favorite>>
    fun getFavoriteCount(): Single<Int>

    fun removeFromFavorites(trackId: Long): Completable
    fun saveInFavorite(music: Music): Completable
}

class FavoriteInteractorImpl @Inject constructor(
    private val favoriteRepository: FavoriteRepository,
    private val musicRepository: MusicRepository
) : FavoriteInteractor {

    override fun isFavorite(dsTrackid: Long): Single<Boolean> =
        favoriteRepository.isFavorite(dsTrackid)

    override fun saveInFavorite(music: Music): Completable =
        musicRepository.save(
            musicEntity = MusicEntity(
                musicId = music.musicId,
                name = music.trackName,
                artworkUrl = music.artworkUrl,
                releaseDate = music.releaseDate,
                isStreamable = music.streamable,
                trackTimeMillis = music.trackTimeMillis,
                previewUrl = music.previewUrl
            ).also { entity ->
                music.musicArtist?.let { entity.artist = ArtistEntity.from(it) }
                music.musicCollection?.let { entity.collection = CollectionEntity.from(it)}
            }
        ).flatMapCompletable { musicId ->
            favoriteRepository.save(musicId)
        }


    override fun removeFromFavorites(musicId: Long): Completable =
        favoriteRepository.remove(musicId)


    override fun getFavoriteMusics(): Flowable<List<Favorite>> =
        favoriteRepository.getAll()
            .map { favoriteList ->
                favoriteList.map { favorite ->
                    favorite.music = musicRepository.get(favorite.musicId).blockingGet()
                }
                return@map favoriteList
            }


    override fun getFavoriteCount(): Single<Int> = favoriteRepository.getCount()
}