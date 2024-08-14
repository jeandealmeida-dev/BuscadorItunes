package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PlaylistViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: PlaylistViewModel

    @MockK
    private lateinit var interactor: PlaylistInteractor

    private lateinit var playlistListStateObserver: Observer<PlaylistListState>
    private lateinit var playlistDeleteStateObserver: Observer<PlaylistDeleteState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PlaylistViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            interactor = interactor
        )
        playlistListStateObserver = spyk<Observer<PlaylistListState>>(CustomSafeObserver { })
        viewModel.playlistListState.observeForever(playlistListStateObserver)

        playlistDeleteStateObserver = spyk<Observer<PlaylistDeleteState>>(CustomSafeObserver { })
        viewModel.playlistDeleteState.observeForever(playlistDeleteStateObserver)
    }

    @Test
    fun `GIVEN has playlists WHEN user open playlist list screen THEN update state to Empty`() {
        val playlist = Playlist(1, "title", "description")
        val playlists = listOf<Playlist>(playlist)

        every { interactor.getPlaylist() } returns Flowable.just(playlists)
        viewModel.getPlaylistList()
        verify(timeout = 600) {
            playlistListStateObserver.onChanged(PlaylistListState.Loading)
            playlistListStateObserver.onChanged(PlaylistListState.Success(playlists))
        }
    }

//    @Test
//    fun `GIVEN doesnt have playlists WHEN user open playlist list screen THEN update state to Empty`() {
//        val playlists = listOf<Playlist>()
//
//        every { interactor.getPlaylist() } returns Flowable.just(playlists)
//        viewModel.getPlaylistList()
//        verify(timeout = 600) {
//            playlistListStateObserver.onChanged(PlaylistListState.Loading)
//            playlistListStateObserver.onChanged(PlaylistListState.Empty)
//        }
//    }

    @Test
    fun `GIVEN something is wrong WHEN user open playlist list screen THEN update state to Empty`() {
        every { interactor.getPlaylist() } returns Flowable.error(Throwable())
        viewModel.getPlaylistList()
        verify(timeout = 600) {
            playlistListStateObserver.onChanged(PlaylistListState.Loading)
            playlistListStateObserver.onChanged(PlaylistListState.Error)
        }
    }

    @Test
    fun `GIVEN has playlists WHEN user delete playlist screen THEN update state to Success`() {
        every { interactor.deletePlaylist(any()) } returns Completable.complete()
        viewModel.deletePlaylist(1)
        verify(timeout = 600) {
            playlistDeleteStateObserver.onChanged(PlaylistDeleteState.Loading)
            playlistDeleteStateObserver.onChanged(PlaylistDeleteState.Success)
        }
    }

    @Test
    fun `GIVEN something is wrong WHEN user delete playlist THEN update state to Error`() {
        every { interactor.deletePlaylist(any()) } returns Completable.error(Throwable())
        viewModel.deletePlaylist(playlistId = 0)
        verify(timeout = 600) {
            playlistDeleteStateObserver.onChanged(PlaylistDeleteState.Loading)
            playlistDeleteStateObserver.onChanged(PlaylistDeleteState.Error)
        }
    }
}