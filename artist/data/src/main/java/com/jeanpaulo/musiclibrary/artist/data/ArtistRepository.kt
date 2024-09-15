package com.jeanpaulo.musiclibrary.artist.data

import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.repository.database.dao.ArtistDao
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.musiclibrary.core.repository.remote.response.ArtistResponse
import com.jeanpaulo.musiclibrary.core.repository.remote.response.JsonResponseParser
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface ArtistRepository {
    fun getArtist(id: Long): Single<ArtistResponse>
}

class ArtistRepositoryImpl @Inject constructor(
    private val itunesService: ItunesService,
    private val artistDao: ArtistDao,
    private val jsonParser: JsonResponseParser
) : ArtistRepository {

    override fun getArtist(id: Long): Single<ArtistResponse> =
        itunesService.lookUp(
            id = id,
            entity = ItunesService.SONG_ENTITY,
            limit = 5,
            sort = ItunesService.POPULAR_SORT
        ).map { response ->
            if (response.result.isEmpty()) {
                throw EmptyResultException()
            }

            var artist: ArtistResponse? = null
            response.result.mapIndexed { index, response ->
                if (index == 0) {
                    artist = jsonParser.parse(
                        map = response,
                        type = ArtistResponse::class
                    )
                } else {
                    jsonParser.parse(
                        map = response,
                        type = MusicResponse::class
                    )?.let {
                        artist?.addPopularMusic(it)
                    }
                }
            }
            artist?.let {
                return@map it
            } ?: throw EmptyResultException()
        }
}