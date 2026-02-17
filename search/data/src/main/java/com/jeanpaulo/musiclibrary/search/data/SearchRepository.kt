package com.jeanpaulo.musiclibrary.search.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import io.reactivex.rxjava3.core.Flowable
import kotlinx.coroutines.rx3.asFlowable
import javax.inject.Inject

interface SearchRepository {
    fun getSearchResults(query: String): Flowable<PagingData<Music>>
}

class SearchRepositoryImpl @Inject constructor(
    private val service: ItunesService
) : SearchRepository {

    override fun getSearchResults(query: String): Flowable<PagingData<Music>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            initialLoadSize = PAGE_SIZE,
            maxSize = MAX_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            MusicPagingSource(
                service = service,
                query = query
            )
        }
    ).flow.asFlowable()

    companion object {
        const val PAGE_SIZE = 15
        const val MAX_SIZE = 100
    }
}