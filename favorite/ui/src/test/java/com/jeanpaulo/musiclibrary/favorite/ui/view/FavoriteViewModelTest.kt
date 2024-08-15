package com.jeanpaulo.musiclibrary.favorite.ui.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.favorite.ui.favoriteList
import com.jeanpaulo.musiclibrary.favorite.ui.song1
import com.jeanpaulo.musiclibrary.favorite.ui.songList
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var interactor: FavoriteInteractor

    private val scheduler: Scheduler = Schedulers.trampoline()
    private lateinit var favoriteViewModel: FavoriteViewModel
    private lateinit var stateObserver: Observer<FavoriteState>

    private val timeout = 500L

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        favoriteViewModel = FavoriteViewModel(scheduler, scheduler, interactor)

        stateObserver = spyk<Observer<FavoriteState>>(CustomSafeObserver { })
        favoriteViewModel.favoriteState.observeForever(stateObserver)
    }

    @Test
    fun `GIVEN favorite musics fetched successfully WHEN getFavoriteList is called THEN state is loaded`() {
        // GIVEN
        every { interactor.getFavoriteMusics() } returns Flowable.just(favoriteList)

        // WHEN
        favoriteViewModel.getFavoriteList()

        // THEN
        verify(timeout = timeout) {
            stateObserver.onChanged(FavoriteState.Loading)
            stateObserver.onChanged(FavoriteState.Loaded(songList))
        }
    }

    @Test
    fun `GIVEN an error occurs WHEN getFavoriteList is called THEN state is not changed`() {
        // GIVEN
        val error = Throwable("An error occurred")
        every { interactor.getFavoriteMusics() } returns Flowable.error(error)

        // WHEN
        favoriteViewModel.getFavoriteList()

        // THEN
        verify(timeout = timeout) {
            stateObserver.onChanged(FavoriteState.Loading)
            stateObserver.onChanged(FavoriteState.Error)
        }
    }

    @Test
    fun `GIVEN song is removed successfully WHEN remove is called THEN state is removed`() {
        // GIVEN
        every { interactor.removeFromFavorites(song1.musicId) } returns Completable.complete()

        // WHEN
        favoriteViewModel.remove(song1)

        // THEN
        verify(timeout = timeout) { stateObserver.onChanged(FavoriteState.Removed(song1)) }
    }
}