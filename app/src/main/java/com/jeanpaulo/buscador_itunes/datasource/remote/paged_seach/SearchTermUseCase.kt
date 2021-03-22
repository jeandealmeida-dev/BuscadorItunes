package com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach

import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import kotlinx.coroutines.runBlocking

class SearchTermUseCase(
    private val dataSource: MusicDataSource,
    private val term: String,
    private val mediaType: String
) {

    fun execute(
        offset: Int,
        limit: Int
    ): Result<ItunesResponse> {
        return runBlocking { transform(offset, limit) }
    }

    private suspend fun transform(
        offset: Int,
        limit: Int
    ): Result<ItunesResponse> {
        val response = dataSource.searchMusic(term, mediaType, offset, limit)
        return response
    }
}