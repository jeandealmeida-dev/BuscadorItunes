package com.jeanpaulo.buscador_itunes.repository.remote

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.repository.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.repository.remote.util.ItunesResponse2
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import com.jeanpaulo.buscador_itunes.model.util.Result as Result1

/**
 * Interface to the data layer.
 */
interface MusicRemoteDataSource {

    @GET("search")
    fun searchMusic(@Query("term") term: String, @Query("mediaType") mediaType: String, @Query("offset") offset: Int, @Query("limit") limit: Int) : Single<ItunesResponse>

    @GET("lookup")
    fun getCollection(@Query("id") term: Long, @Query("entity") mediaType: String) : ItunesResponse2
}