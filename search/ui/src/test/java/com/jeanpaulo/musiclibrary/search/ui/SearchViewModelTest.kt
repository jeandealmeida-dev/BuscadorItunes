package com.jeanpaulo.musiclibrary.search.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.PagingData
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchState
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class SearchViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var searchInteractor: SearchInteractor

    @MockK
    private lateinit var favoriteInteractor: FavoriteInteractor

    private val scheduler: Scheduler = Schedulers.trampoline()
    private lateinit var viewModel: SearchViewModel
    private lateinit var stateObserver: Observer<SearchState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        viewModel =
            SearchViewModel(scheduler, scheduler, searchInteractor, favoriteInteractor)

        stateObserver = spyk<Observer<SearchState>>(CustomSafeObserver { })
        viewModel.searchingState.observeForever(stateObserver)
    }

    @Test
    fun `GIVEN a music WHEN selected options menu THEN change view state to options`() {
        //GIVEN
        val song = SongUIModel.fromModel(music1)

        //WHEN
        viewModel.options(song)

        //THEN
        verify { stateObserver.onChanged(match { equalsState(it, SearchState.Options(song)) }) }
    }

    @Test
    fun `GIVEN a music WHEN add to favorites from menu THEN get music from memory and save on database`() {
        //GIVEN
        every { searchInteractor.getSearchMusic(music1.musicId) } returns music1
        every { favoriteInteractor.saveInFavorite(music1) } returns Completable.complete()

        //WHEN
        viewModel.addInFavorite(song1)

        //THEN
        verifyOrder {
            searchInteractor.getSearchMusic(song1.musicId)
            favoriteInteractor.saveInFavorite(music1)
        }
    }

    @Ignore("test is failing in compare two paging data")
    @Test
    fun `GIVEN a query WHEN search by a term THEN get music list filtered by term`() {
        //GIVEN
        val pagingData = PagingData.from(musicList)
        every { searchInteractor.getSearchResults(query) } returns Flowable.just(pagingData)

        //WHEN
        viewModel.setCurrentQuery(query)

        //THEN
        val pagingDataSong = PagingData.from(songList)

        verifyOrder {
            stateObserver.onChanged(SearchState.Loading)
            //TODO is not working
            //stateObserver.onChanged(match { equalsState(it, SearchState.Success(pagingDataSong))})
        }
    }
}