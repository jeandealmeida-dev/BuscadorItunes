package com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach

import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.model.Music
import kotlinx.coroutines.runBlocking

class SearchTermUseCase(
    private val dataSource: MusicDataSource,
    private val term: String,
    private val mediaType: String
) {

    fun execute(
        offset: Int,
        limit: Int
    ): Result<List<Music>> {
        return runBlocking { transform(offset, limit) }
    }

    private suspend fun transform(
        offset: Int,
        limit: Int
    ): Result<List<Music>> {
        val response = dataSource.searchMusic(term, mediaType, offset, limit)
        return response
    }
}