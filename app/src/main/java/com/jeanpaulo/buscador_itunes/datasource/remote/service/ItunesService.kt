package com.jeanpaulo.buscador_itunes.datasource.remote.service

import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to the data layer.
 */
interface ItunesService {

    @GET("search")
    suspend fun searchMusic(
        @Query("term") term: String,
        @Query("mediaType") mediaType: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ItunesResponse>

    @GET("lookup")
    suspend fun getCollection(
        @Query("id") term: Long,
        @Query("entity") mediaType: String
    ): ItunesResponse2
}