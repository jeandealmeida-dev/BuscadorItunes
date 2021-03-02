package com.jeanpaulo.buscador_itunes.service

import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("search")
    fun searchMusic(@Query("term") term: String, @Query("mediaType") mediaType: String, @Query("offset") offset: Int, @Query("limit") limit: Int) : Single<ItunesResponse>
}