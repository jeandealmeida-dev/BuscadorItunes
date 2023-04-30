package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistCreateInteractor
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

class PlaylistCreateViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: PlaylistCreateViewModel

    @MockK
    private lateinit var interactor: PlaylistCreateInteractor

    private lateinit var favoriteStateObserver: Observer<PlaylistCreateState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PlaylistCreateViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            playlistInteractor = interactor
        )
        favoriteStateObserver = spyk<Observer<PlaylistCreateState>>(CustomSafeObserver { })
        viewModel.playlistCreateState.observeForever(favoriteStateObserver)
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has favorites THEN update state to Success with favorite list`() {
        /*val music = Music(1, 0L, "", "", Date(), true, 0L, "")
        val favorites = listOf(PlaylistCreate(1).apply { this.music = music })

        every { interactor.getPlaylistCreateMusics() } returns Flowable.just(favorites)
        viewModel.getPlaylistCreateList()
        favoriteStateObserver.onChanged(PlaylistCreateState.Success(listOf(music)))*/
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has NOT favorites THEN update state to Success but empty`() {
/*every { interactor.getPlaylistCreateMusics() } returns Flowable.error(EmptyResultException())
viewModel.getPlaylistCreateList()
favoriteStateObserver.onChanged(PlaylistCreateState.Success(emptyList()))*/
    }

    @Test
    fun `GIVEN user open favorite tab WHEN get error THEN update state to Error`() {
/*every { interactor.getPlaylistCreateMusics() } returns Flowable.error(NotImplementedError())
viewModel.getPlaylistCreateList()
favoriteStateObserver.onChanged(PlaylistCreateState.Error)*/
    }
}