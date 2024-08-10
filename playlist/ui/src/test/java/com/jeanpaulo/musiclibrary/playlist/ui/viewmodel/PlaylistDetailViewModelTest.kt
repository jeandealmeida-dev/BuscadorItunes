package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistDetailInteractor
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

class PlaylistDetailViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: PlaylistDetailViewModel

    @MockK
    private lateinit var interactor: PlaylistDetailInteractor

    private lateinit var playlistDetailStateObserver: Observer<PlaylistDetailState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = PlaylistDetailViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            interactor = interactor
        )
        playlistDetailStateObserver = spyk<Observer<PlaylistDetailState>>(CustomSafeObserver { })
        viewModel.playlistDetailState.observeForever(playlistDetailStateObserver)
    }
}