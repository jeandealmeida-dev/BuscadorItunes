package com.jeanpaulo.musiclibrary.music.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.music.data.MusicRepository
import com.jeanpaulo.musiclibrary.music.domain.MusicInteractor.Companion.SONG
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MusicInteractor {
    fun getMusic(id: Long): Single<Music>
    fun findLocal(remoteId: Long): Single<Music>

    fun saveMusicInFavorite(music: Music): Completable
    fun removeFromFavorites(musicId: Long): Completable
    fun isFavorite(remoteId: Long): Single<Boolean>

    companion object {
        const val SONG = "song"
    }
}

class MusicInteractorImpl @Inject constructor(
    private val musicRepository: MusicRepository,
    private val favoriteRepository: FavoriteRepository,
) : MusicInteractor {

    override fun getMusic(id: Long): Single<Music> =
        musicRepository.lookup(id, SONG)

    override fun findLocal(remoteId: Long): Single<Music> =
        musicRepository.findLocal(remoteId)

    override fun saveMusicInFavorite(music: Music): Completable =
        musicRepository.save(MusicEntity.fromModel(music)).concatMapCompletable {
            favoriteRepository.save(musicId = it)
        }

    override fun removeFromFavorites(musicId: Long): Completable =
        favoriteRepository.remove(musicId)

    override fun isFavorite(remoteId: Long): Single<Boolean> =
        favoriteRepository.isFavorite(remoteId)
}