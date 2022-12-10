package com.jeanpaulo.buscador_itunes.app.music.search.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.model.SearchMusicUIModel
import com.jeanpaulo.buscador_itunes.app.music_detail.domain.SearchParams
import javax.inject.Inject
import javax.inject.Singleton

interface SearchRepository {
    fun getSearchResults(query: String): LiveData<PagingData<SearchMusicUIModel>>
}

class SearchRepositoryImpl @Inject constructor(
    private val service: ItunesService
) : SearchRepository {
    override fun getSearchResults(query: String): LiveData<PagingData<SearchMusicUIModel>> = Pager(
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