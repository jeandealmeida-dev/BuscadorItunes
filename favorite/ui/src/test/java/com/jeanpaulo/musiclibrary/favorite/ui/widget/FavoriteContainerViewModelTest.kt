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
import com.jeanpaulo.musiclibrary.favorite.ui.widgets.FavoriteContainerViewModel
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

    private val timeout = 600L

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
        verify(timeout = timeout) {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(match {
                equalViewState(
                    it,
                    ViewState.Success(musicCount)
                )
            })
        }
    }

    @Test
    fun `GIVEN an error occurs WHEN getFavoriteCount is called THEN state change to Error`() {
        // GIVEN
        val error = Throwable("An error occurred")
        every { interactor.getFavoriteCount() } returns Single.error(error)

        // WHEN
        favoriteContainerViewModel.getFavoriteCount()

        // THEN
        verify(timeout = timeout) {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(ViewState.Error)
        }
    }

    @Test
    fun `GIVEN no music on favorite WHEN getFavoriteCount is called THEN state changed to Empty`() {
        // GIVEN
        val error = EmptyResultException()
        every { interactor.getFavoriteCount() } returns Single.error(error)

        // WHEN
        favoriteContainerViewModel.getFavoriteCount()

        // THEN
        verify(timeout = timeout) {
            stateObserver.onChanged(ViewState.Loading)
            stateObserver.onChanged(ViewState.Empty)
        }
    }

    // Private

    private fun equalViewState(
        state1: ViewState<Int>,
        state2: ViewState<Int>
    ): Boolean {
        return when {
            state1 is ViewState.Success && state2 is ViewState.Success -> state1.data == state2.data
            else -> state1 == state2
        }
    }
}