package com.jeanpaulo.musiclibrary.core.repository.remote

import com.jeanpaulo.musiclibrary.core.repository.remote.response.LookUpResponse
import com.jeanpaulo.musiclibrary.core.repository.remote.response.SearchMusicResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface to the data layer.
 */
interface ItunesService {

    @GET("search")
    fun searchMusic(
        @Query("term") term: String,
        @Query("mediaType") mediaType: String,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<SearchMusicResponse>

    @GET("lookup")
    fun lookUp(
        @Query("id") id: Long,
        @Query("entity") entity: String,
        @Query("limit") limit: Int? = null,
        @Query("sort") sort: String? = null,
    ): Single<LookUpResponse>

    companion object {
        const val SONG_ENTITY = "song"
        const val ARTIST_ENTITY = "musicArtist"
        const val RECENT_SORT = "recent"
        const val POPULAR_SORT = "popular"
    }
}