package com.jeanpaulo.musiclibrary.search.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.musiclibrary.core.repository.remote.mapper.convertToMusic
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import java.lang.Exception

private const val SEARCH_STARTING_PAGE_INDEX = 1

class SearchPagingSource(
    private val itunesService: ItunesService,
    private val query: String
) : PagingSource<Int, MusicResponse>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MusicResponse> {
        val position = params.key ?: SEARCH_STARTING_PAGE_INDEX
        return try {
            val listMusic = itunesService.searchMusic(
                term = query,
                mediaType = SearchParams.SONG_MEDIA_TYPE,
                offset = position,
                limit = params.loadSize
            ).blockingGet().result

            LoadResult.Page(
                data = listMusic,
                prevKey = if (position == SEARCH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (listMusic.isEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MusicResponse>): Int? {
        return null
    }
}