package com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.paged_search

import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.Result
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.IDataSource
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import kotlinx.coroutines.runBlocking

class SearchTermUseCase(
    private val dataSource: IDataSource,
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