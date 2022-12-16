package com.jeanpaulo.musiclibrary.search.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.core.repository.remote.mapper.convertToMusic
import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import com.jeanpaulo.musiclibrary.search.domain.model.SearchMusic
import com.jeanpaulo.musiclibrary.search.domain.model.mapper.toSearchMusic
import javax.inject.Inject

interface SearchInteractor {
    fun getSearchResults(query: String): LiveData<PagingData<SearchMusic>>
}

class SearchInteractorImpl @Inject constructor(
    val repository: SearchRepository
) : SearchInteractor {

    override fun getSearchResults(query: String): LiveData<PagingData<SearchMusic>> =
        repository.getSearchResults(query)
            .map { pagingData -> pagingData.map { it.toSearchMusic() } }
}
