package com.jeanpaulo.musiclibrary.search.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import javax.inject.Inject

interface SearchRepository {
    fun getSearchResults(query: String): LiveData<PagingData<MusicResponse>>
}

class SearchRepositoryImpl @Inject constructor(
    private val service: ItunesService
) : SearchRepository {
    override fun getSearchResults(query: String): LiveData<PagingData<MusicResponse>> = Pager(
        config = PagingConfig(
            pageSize = SearchParams.SEARCH_PAGE_SIZE,
            maxSize = SearchParams.MAX_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            SearchPagingSource(service, query)
        }
    ).liveData
}