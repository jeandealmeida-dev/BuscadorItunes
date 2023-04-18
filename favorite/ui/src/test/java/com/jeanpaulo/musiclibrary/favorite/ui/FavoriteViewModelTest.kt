package com.jeanpaulo.musiclibrary.favorite.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jeanpaulo.musiclibrary.commons.CustomSafeObserver
import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
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

class FavoriteViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = Schedulers.trampoline()

    private lateinit var viewModel: FavoriteViewModel

    @MockK
    private lateinit var interactor: FavoriteInteractor

    private lateinit var favoriteStateObserver: Observer<FavoriteState>

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        viewModel = FavoriteViewModel(
            mainScheduler = scheduler,
            ioScheduler = scheduler,
            interactor = interactor
        )
        favoriteStateObserver = spyk<Observer<FavoriteState>>(CustomSafeObserver { })
        viewModel.favoriteState.observeForever(favoriteStateObserver)
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has favorites THEN update state to Success with favorite list`() {
        val music = Music(1, 0L, "", "", Date(), true, 0L, "")
        val favorites = listOf(Favorite(1).apply { this.music = music })

        every { interactor.getFavoriteMusics() } returns Flowable.just(favorites)
        viewModel.getFavoriteList()
        favoriteStateObserver.onChanged(FavoriteState.Success(listOf(music)))
    }

    @Test
    fun `GIVEN user open favorite tab WHEN it has NOT favorites THEN update state to Success but empty`() {
        every { interactor.getFavoriteMusics() } returns Flowable.error(EmptyResultException())
        viewModel.getFavoriteList()
        favoriteStateObserver.onChanged(FavoriteState.Success(emptyList()))
    }

    @Test
    fun `GIVEN user open favorite tab WHEN get error THEN update state to Error`() {
        every { interactor.getFavoriteMusics() } returns Flowable.error(NotImplementedError())
        viewModel.getFavoriteList()
        favoriteStateObserver.onChanged(FavoriteState.Error)
    }
}