package com.jeanpaulo.buscador_itunes.music.mvvm.data.remote

import com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.response.LookUpResponse
import com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.response.SearchMusicResponse
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
    ): Response<SearchMusicResponse>

    @GET("lookup")
    suspend fun lookUp(
        @Query("id") term: Long,
        @Query("entity") mediaType: String
    ): Response<LookUpResponse>
}