package com.jeanpaulo.musiclibrary.search.domain

import androidx.paging.PagingData
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.rxjava3.core.Flowable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

class SearchInteractorTest {

    private lateinit var interactor: SearchInteractor

    @MockK
    private lateinit var repository: SearchRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        interactor = SearchInteractorImpl(repository)
    }

    @Ignore("Its not working")
    @Test
    fun `GIVEN a query WHEN getSearchResults is called THEN it should return the expected PagingData`() {
        // GIVEN
        val pagingData = PagingData.from(musicList)
        every { repository.getSearchResults(query) } returns Flowable.just(pagingData)

        // WHEN
        val testObserver = interactor.getSearchResults(query).test()

        // THEN
        verify { repository.getSearchResults(query) }
        testObserver.assertComplete()

        assertEquals(musicList, interactor.getSearchMusicList())
    }

    @Test
    fun `GIVEN a query WHEN getSearchResults is called THEN it should return empty PagingData`() {
        // GIVEN
        val pagingData = PagingData.empty<Music>()
        every { repository.getSearchResults(query) } returns Flowable.just(pagingData)

        // WHEN
        val testObserver = interactor.getSearchResults(query).test()

        // THEN
        verify { repository.getSearchResults(query) }
        testObserver.assertComplete()

        assertEquals(interactor.getSearchMusicList(), emptyList<Music>())
    }


    @Test
    fun `GIVEN a music ID WHEN getSearchMusic is called THEN it should return the expected Music`() {
        // GIVEN
        val searchInteractorImpl = interactor as SearchInteractorImpl
        searchInteractorImpl.updateMusicList(musicList)

        // WHEN
        val result = interactor.getSearchMusic(1)

        // THEN
        val expectedResult = musicList.find { it.musicId == 1L }
        assertEquals(expectedResult, result)
    }
}