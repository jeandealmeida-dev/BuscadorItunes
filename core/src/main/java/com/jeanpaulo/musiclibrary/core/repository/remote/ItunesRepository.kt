package com.jeanpaulo.musiclibrary.core.repository.remote

import com.jeanpaulo.musiclibrary.core.repository.remote.mapper.convertToMusic
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MusicDetailRepository {
    fun lookup(musicId: Long, songMediaType: String): Single<Music>
}

class MusicDetailRepositoryImpl @Inject constructor(
    val itunesService: ItunesService,
) : MusicDetailRepository {

    override fun lookup(term: Long, mediaType: String): Single<Music> =
        itunesService.lookUp(term, mediaType).map { response -> response.result[0].convertToMusic() }

}