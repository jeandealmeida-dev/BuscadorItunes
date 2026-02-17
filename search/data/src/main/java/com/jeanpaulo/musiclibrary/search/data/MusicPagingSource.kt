package com.jeanpaulo.musiclibrary.search.data

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import io.reactivex.rxjava3.core.Single

class MusicPagingSource(
    private val service: ItunesService,
    private val query: String
) : RxPagingSource<Int, Music>() {
    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Music>> {
        val position = params.key ?: 0

        return service.searchMusic(
            term = query,
            mediaType = SONG_MEDIA_TYPE,
            offset = position * params.loadSize,
            limit = params.loadSize
        )
            .map { response ->
                LoadResult.Page(
                    data = response.result.map { it.toModel() },
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = if (response.result.isEmpty()) null else position + 1
                ) as LoadResult<Int, Music>
            }
            .onErrorReturn { LoadResult.Error(it) }
    }

    override fun getRefreshKey(state: PagingState<Int, Music>): Int? {
        return null
    }

    companion object {
        const val SONG_MEDIA_TYPE = "song"
        const val SEARCH_STARTING_PAGE_INDEX = 0
    }
}