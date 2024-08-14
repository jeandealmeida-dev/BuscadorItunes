package com.jeanpaulo.musiclibrary.search.data

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkConstructor
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class SearchRepositoryTest {

    @MockK
    private lateinit var service: ItunesService

    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        repository = SearchRepositoryImpl(service)
    }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `GIVEN valid query WHEN getSearchResults is called THEN return PagingData of Music`() =
//        runTest {
//            // GIVEN
//            val pagingData = PagingData.from(musicListResponse)
//            mockkConstructor(SearchPagingSource::class)
//            every { anyConstructed<SearchPagingSource>().load(any()) } returns PagingSource.LoadResult.Page(
//                data = musicListResponse,
//                prevKey = null,
//                nextKey = 1
//            )
//
//            every { service.searchMusic(query, "song", 0, 15) } returns Single.just(
//                musicResponse
//            )
//
//            // WHEN
//            val result = repository.getSearchResults(query)
//                .subscribeOn(Schedulers.trampoline())
//                .blockingFirst()
//
//            // THEN
//            assert(result is PagingData<MusicResponse>)
//        }
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    @Test
//    fun `GIVEN empty result WHEN getSearchResults is called THEN return empty PagingData`() =
//        runBlockingTest {
//            // Arrange
//            val pagingData = PagingData.from(emptyList<MusicResponse>())
//            mockkConstructor(SearchPagingSource::class)
//            every { service.searchMusic(query, "song", 0, 15) } returns Single.just(musicResponse)
//            every { anyConstructed<SearchPagingSource>().load(any()) } returns PagingSource.LoadResult.Page(
//                data = emptyList(),
//                prevKey = null,
//                nextKey = null
//            )
//
//            // Act
//            val result = repository.getSearchResults(query)
//                .subscribeOn(Schedulers.trampoline())
//                .blockingFirst()
//
//            // Assert
//            assert(result is PagingData<Music>)
//            assertEquals(0, (result as PagingData<*>).itemCount)
//        }

    @Ignore("Its not working")
    @Test
    fun `GIVEN a query WHEN getSearchResults is called THEN return expected PagingData`() {
        // Given
        val query = "test query"
        every { service.searchMusic(query, any(), any(), any()) } returns Single.just(musicResponse)

        // When
        val testObserver = repository.getSearchResults(query)
            .subscribeOn(Schedulers.trampoline())
            .test()

        // Then
        testObserver.assertNoErrors()
        testObserver.assertValue { pagingDataResult ->
            pagingDataResult == PagingData.from(musicListResponse)
        }
    }
}