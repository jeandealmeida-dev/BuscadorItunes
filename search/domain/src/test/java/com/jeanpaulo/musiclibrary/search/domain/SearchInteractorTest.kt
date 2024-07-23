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
import org.junit.Test
import java.util.concurrent.Flow

class SearchInteractorTest {

    private lateinit var searchInteractor: SearchInteractor

    @MockK
    private lateinit var searchRepository: SearchRepository

    @Before
    fun setup(){
        MockKAnnotations.init(this)

        searchInteractor = SearchInteractorImpl(searchRepository)
    }

    @Test
    fun `GIVEN search was not request WHEN get music from search list THEN return null`(){
        val musicId = 1L
        val musicResponse = searchInteractor.getSearchMusic(musicId)
        assertEquals(musicResponse, null)
    }
}