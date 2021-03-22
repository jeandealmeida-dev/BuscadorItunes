package com.jeanpaulo.buscador_itunes.datasource.remote

import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse2

/**
 * Interface to the data layer.
 */
interface MusicRemoteDataSource {

    suspend fun searchMusic(term: String, mediaType: String, offset: Int, limit: Int): com.jeanpaulo.buscador_itunes.model.util.Result<ItunesResponse>

    suspend fun getCollection(term: Long, mediaType: String): ItunesResponse2
}