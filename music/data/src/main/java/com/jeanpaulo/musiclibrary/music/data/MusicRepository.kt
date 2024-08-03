package com.jeanpaulo.musiclibrary.music.data

import androidx.room.rxjava3.EmptyResultSetException
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.database.dao.ArtistDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.CollectionDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.MusicDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.functions.BiFunction
import javax.inject.Inject

interface MusicRepository {
    fun lookup(musicId: Long, songMediaType: String): Single<Music>
    fun save(musicEntity: MusicEntity): Single<Long>
    fun findLocal(remoteId: Long): Single<Music>
    fun get(musicId: Long): Single<Music>
}

class MusicRepositoryImpl @Inject constructor(
    private val itunesService: ItunesService,
    private val musicDao: MusicDao,
    private val artistDao: ArtistDao,
    private val collectionDao: CollectionDao,
) : MusicRepository {

    override fun lookup(term: Long, mediaType: String): Single<Music> =
        itunesService.lookUp(term, mediaType)
            .map { response ->
                if(response.result.isNotEmpty())
                    response.result[0].toModel()
                else
                    throw EmptyResultException()
            }

    override fun save(musicEntity: MusicEntity): Single<Long> {
        return when {
            musicEntity.artist != null && musicEntity.collection != null -> {
                Single.zip<Long, Long, Long>(
                    artistDao.insertArtist(musicEntity.artist!!),
                    collectionDao.insertCollection(musicEntity.collection!!),
                    BiFunction<Long, Long, Long> { newArtistId, newCollectionId ->
                        musicDao.insertMusic(
                            musicEntity.apply {
                                artistId = newArtistId
                                collectionId = newCollectionId
                            }
                        ).blockingGet()
                    }
                )
            }

            else -> {
                musicDao.insertMusic(music = musicEntity)
            }
        }
    }


    override fun findLocal(remoteId: Long): Single<Music> =
        musicDao.getMusicByRemoteId(remoteId)
            .flatMap { musicEntity ->
                if (musicEntity.artistId > 0) {
                    musicEntity.artist = artistDao
                        .getArtistById(musicEntity.artistId)
                        .blockingGet()
                }
                if (musicEntity.collectionId > 0) {
                    musicEntity.collection = collectionDao
                        .getCollectionById(musicEntity.collectionId)
                        .blockingGet()
                }

                Single.just(musicEntity.toModel())
            }
            .onErrorResumeNext {
                throw when (it) {
                    is EmptyResultSetException -> {
                        EmptyResultException()
                    }

                    else -> {
                        it
                    }
                }
            }

    override fun get(musicId: Long): Single<Music> =
        musicDao.getMusicById(musicId)
            .map { musicEntity ->
                musicEntity.artist =
                    artistDao.getArtistById(musicEntity.artistId).blockingGet()
                musicEntity.collection =
                    collectionDao.getCollectionById(musicEntity.collectionId).blockingGet()
                musicEntity.toModel()
            }
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