package com.jeanpaulo.musiclibrary.settings.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK
    private lateinit var interactor: PlaylistInteractor

    private val scheduler: Scheduler = Schedulers.trampoline()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = SettingsViewModel(scheduler, scheduler, interactor)
    }

    @Test
    fun `GIVEN delete all database WHEN clearDatabase is called THEN state is success`() {
        // GIVEN
        every { interactor.deleteAllPlaylists() } returns Completable.complete()

        // WHEN
        viewModel.clearDatabase()

        // THEN
        interactor.deleteAllPlaylists()
            .test()
            .assertComplete()
    }


}