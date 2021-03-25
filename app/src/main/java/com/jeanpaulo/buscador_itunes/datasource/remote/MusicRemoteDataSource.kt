package com.jeanpaulo.buscador_itunes.datasource.remote

import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse2
import com.jeanpaulo.buscador_itunes.model.util.Result
import retrofit2.Response

/**
 * Interface to the data layer.
 */
interface MusicRemoteDataSource {

    suspend fun searchMusic(
        term: String,
        mediaType: String,
        offset: Int,
        limit: Int
    ): Result<ItunesResponse>

    suspend fun lookup(
        term: Long,
        mediaType: String
    ): Result<ItunesResponse2>
}