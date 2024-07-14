package com.jeanpaulo.musiclibrary.search.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface SearchRepository {
    fun getSearchResults(query: String): Flow<PagingData<Music>>
}

class SearchRepositoryImpl @Inject constructor(
    private val service: ItunesService
) : SearchRepository {

    private val pagingConfig = PagingConfig(
        pageSize = SearchParams.PAGE_SIZE,
        initialLoadSize = SearchParams.PAGE_SIZE,
        maxSize = SearchParams.MAX_SIZE,
        enablePlaceholders = false,
    )

    override fun getSearchResults(query: String): Flow<PagingData<Music>> = Pager(
        config = pagingConfig,
        pagingSourceFactory = { SearchPagingSource(service, query) }
    ).flow.map { pagingData ->
        pagingData.map { it.toModel() }
    }

}