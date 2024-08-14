package com.jeanpaulo.musiclibrary.search.data

import androidx.paging.PagingSource
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SearchPagingSourceTest {

    @MockK
    private lateinit var itunesService: ItunesService

    private lateinit var searchPagingSource: SearchPagingSource

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        searchPagingSource = SearchPagingSource(itunesService, query)
    }

    @Test
    fun `GIVEN service returns list of music WHEN load is called THEN return LoadResult_Page`() =
        runBlocking {
            // Arrange
            val loadParams = PagingSource.LoadParams.Refresh<Int>(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
            every {
                itunesService.searchMusic(
                    query,
                    SearchPagingSource.SONG_MEDIA_TYPE,
                    0,
                    2
                )
            } returns Single.just(musicResponse)

            // Act
            val result = searchPagingSource.load(loadParams)

            // Assert
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = musicListResponse,
                    prevKey = null,
                    nextKey = 1
                ),
                result
            )
        }

    @Test
    fun `GIVEN service returns empty list WHEN load is called THEN return LoadResult_Page with nextKey null`() =
        runBlocking {
            // Arrange
            val loadParams = PagingSource.LoadParams.Refresh<Int>(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
            every {
                itunesService.searchMusic(
                    query,
                    SearchPagingSource.SONG_MEDIA_TYPE,
                    0,
                    2
                )
            } returns Single.just(emptyMusicResponse)

            // Act
            val result = searchPagingSource.load(loadParams)

            // Assert
            assertEquals(
                PagingSource.LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                ),
                result
            )
        }

    @Test
    fun `GIVEN service throws exception WHEN load is called THEN return LoadResult_Error`() =
        runBlocking {
            // Arrange
            val loadParams = PagingSource.LoadParams.Refresh<Int>(
                key = null,
                loadSize = 2,
                placeholdersEnabled = false
            )
            val exception = RuntimeException("Network error")
            every {
                itunesService.searchMusic(
                    query,
                    SearchPagingSource.SONG_MEDIA_TYPE,
                    0,
                    2
                )
            } returns Single.error(exception)

            // Act
            val result = searchPagingSource.load(loadParams)

            // Assert
            assert(result is PagingSource.LoadResult.Error)
            assertEquals((result as PagingSource.LoadResult.Error).throwable, exception)
        }
}