package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistCreateInteractor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaylistCreateViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: PlaylistCreateViewModel

    @MockK
    private lateinit var interactor: PlaylistCreateInteractor

    private lateinit var stateObserver: Observer<PlaylistCreateState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PlaylistCreateViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            playlistInteractor = interactor
        )
        stateObserver = spyk<Observer<PlaylistCreateState>>(CustomSafeObserver { })
        viewModel.playlistCreateState.observeForever(stateObserver)
    }

    @Test
    fun `GIVEN user insert valid playlist name and description WHEN create playlist THEN update state to Success`() {
        val longId = 1L
        val title = "title"
        val description = "description"
        every { interactor.savePlaylist(any()) } returns Single.just(longId)
        viewModel.createPlaylist(title, description)

        verify(timeout = 600) {
            stateObserver.onChanged(PlaylistCreateState.Loading)
            stateObserver.onChanged(PlaylistCreateState.Success(longId))
        }
    }

    @Test
    fun `GIVEN user insert valid playlist name but no description WHEN create playlist THEN update state to Success`() {
        val longId = 1L
        val title = "title"
        val description = null
        every { interactor.savePlaylist(any()) } returns Single.just(longId)

        viewModel.createPlaylist(title, description)

        verify(timeout = 600) {
            stateObserver.onChanged(PlaylistCreateState.Loading)
            stateObserver.onChanged(PlaylistCreateState.Success(longId))
        }
    }

//    @Test
//    fun `GIVEN user insert valid playlist name and description WHEN create playlist BUT get an error THEN update state to Error`() {
//        val title = "title"
//        val description = null
//        every { interactor.savePlaylist(any()) } returns Single.error(Throwable())
//        viewModel.createPlaylist(title, description)
//
//        verify {
//            stateObserver.onChanged(PlaylistCreateState.Loading)
//            stateObserver.onChanged(PlaylistCreateState.Error)
//        }
//    }
}