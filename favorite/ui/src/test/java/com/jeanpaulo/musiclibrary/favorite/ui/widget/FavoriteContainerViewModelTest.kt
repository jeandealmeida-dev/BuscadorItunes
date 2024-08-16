package com.jeanpaulo.musiclibrary.favorite.ui.widget

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.favorite.ui.favoriteList
import com.jeanpaulo.musiclibrary.favorite.ui.song1
import com.jeanpaulo.musiclibrary.favorite.ui.songList
import com.jeanpaulo.musiclibrary.favorite.ui.view.FavoriteState
import com.jeanpaulo.musiclibrary.favorite.ui.widgets.FavoriteContainerViewModel
import io.mockk.verifyOrder
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteContainerViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var interactor: FavoriteInteractor

    private val scheduler: Scheduler = Schedulers.trampoline()
    private lateinit var favoriteContainerViewModel: FavoriteContainerViewModel
    private lateinit var stateObserver: Observer<ViewState<Int>>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        favoriteContainerViewModel = FavoriteContainerViewModel(scheduler, scheduler, interactor)

        stateObserver = spyk<Observer<ViewState<Int>>>(CustomSafeObserver { })
        favoriteContainerViewModel.favoriteCountState.observeForever(stateObserver)
    }

    @Test
    fun `GIVEN has favorite musics WHEN getFavoriteCount is called THEN state is Success with music count`() {
        // GIVEN
        val musicCount = 3
        every { interactor.getFavoriteCount() } returns Single.just(musicCount)

        // WHEN
        favoriteContainerViewModel.getFavoriteCount()

        // THEN
        verifyOrder {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(ViewState.Success(musicCount))
        }
    }

    @Test
    fun `GIVEN an error occurs WHEN getFavoriteCount is called THEN state change to Error`() {
        // GIVEN
        val error = Throwable("An error occurred")
        every { interactor.getFavoriteMusics() } returns Flowable.error(error)

        // WHEN
        favoriteContainerViewModel.getFavoriteCount()

        // THEN
        verifyOrder {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(ViewState.Error)
        }
    }

    @Test
    fun `GIVEN no music on favorite WHEN getFavoriteCount is called THEN state changed to Empty`() {
        // GIVEN
        val error = EmptyResultException()
        every { interactor.getFavoriteMusics() } returns Flowable.error(error)

        // WHEN
        favoriteContainerViewModel.getFavoriteCount()

        // THEN
        verifyOrder {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(ViewState.Empty)
        }
    }
}