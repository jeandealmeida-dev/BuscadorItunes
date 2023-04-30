package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//TODO Jean fix test
class PlaylistViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: PlaylistViewModel

    @MockK
    private lateinit var interactor: PlaylistInteractor

    private lateinit var playlistStateObserver: Observer<PlaylistState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PlaylistViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            interactor = interactor
        )
        playlistStateObserver = spyk<Observer<PlaylistState>>(CustomSafeObserver { })
        viewModel.playlistListState.observeForever(playlistStateObserver)
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has favorites THEN update state to Success with favorite list`() {
        /*val music = Music(1, 0L, "", "", Date(), true, 0L, "")
        val favorites = listOf(Favorite(1).apply { this.music = music })

        every { interactor.getFavoriteMusics() } returns Flowable.just(favorites)
        viewModel.getFavoriteList()
        playlistStateObserver.onChanged(PlaylistState.Success(listOf(music)))*/
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has NOT favorites THEN update state to Success but empty`() {
        /*every { interactor.getFavoriteMusics() } returns Flowable.error(EmptyResultException())
        viewModel.getFavoriteList()
        playlistStateObserver.onChanged(PlaylistState.Success(emptyList()))*/
    }

    @Test
    fun `GIVEN user open favorite tab WHEN get error THEN update state to Error`() {
        /*every { interactor.getFavoriteMusics() } returns Flowable.error(NotImplementedError())
        viewModel.getFavoriteList()
        playlistStateObserver.onChanged(PlaylistState.Error)*/
    }
}