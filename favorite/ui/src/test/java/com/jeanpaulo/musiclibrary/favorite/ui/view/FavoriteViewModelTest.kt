package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.music_player.MPService
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.favorite.ui.favoriteList
import com.jeanpaulo.musiclibrary.favorite.ui.song1
import com.jeanpaulo.musiclibrary.favorite.ui.songList
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Ignore
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

    private val timeout = 3000L

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
            stateObserver.onChanged(FavoriteState.Wrapper(ViewState.Loading))
            stateObserver.onChanged(match {
                equalState(
                    it,
                    FavoriteState.Wrapper(ViewState.Success(songList))
                )
            })
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
            stateObserver.onChanged(FavoriteState.Wrapper(ViewState.Loading))
            stateObserver.onChanged(FavoriteState.Wrapper(ViewState.Error))
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

    @Test
    fun `WHEN options is selected THEN state is ShowMusicOptions`() {
        // WHEN
        favoriteViewModel.options(song1)

        // THEN
        verify { stateObserver.onChanged(FavoriteState.ShowMusicOptions(song1)) }
    }

    @Ignore("Its not working: putExtra is not mocked ")
    @Test
    fun `GIVEN song is selected WHEN playSong is called THEN should call play song service method`() {
        // GIVEN
        val context = mockk<Context>(relaxed = true)
        mockkStatic(MPService::class)
        every { MPService.playSong(any(), any()) } just runs

        // WHEN
        favoriteViewModel.playMusic(context, song1)

        // THEN
        verify { MPService.playSong(context, song1.convertToSong()) }
    }

    // Private

    private fun equalListSongViewState(
        state1: ViewState<List<SongUIModel>>,
        state2: ViewState<List<SongUIModel>>
    ): Boolean {
        return when {
            state1 is ViewState.Success && state2 is ViewState.Success ->
                state1.data == state2.data

            else -> state1 == state2
        }
    }

    private fun equalState(state1: FavoriteState, state2: FavoriteState): Boolean {
        return when {
            state1 is FavoriteState.Wrapper && state2 is FavoriteState.Wrapper ->
                equalListSongViewState(state1.viewState, state2.viewState)

            else -> state1 == state2
        }
    }
}