package com.jeanpaulo.musiclibrary.playlist.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractorImpl
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.playlist.data.PlaylistRepository
import com.jeanpaulo.musiclibrary.playlist.data.PlaylistRepositoryImpl
import com.jeanpaulo.musiclibrary.playlist.view.PlaylistFragment
import com.jeanpaulo.musiclibrary.playlist.view.PlaylistViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [PlaylistModule::class]
)
abstract class PlaylistModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): PlaylistFragment
}

@Module
abstract class PlaylistModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistViewModel::class)
    abstract fun provideViewModel(viewModel: PlaylistViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: PlaylistInteractorImpl): PlaylistInteractor

    @Binds
    abstract fun provideRepository(repository: PlaylistRepositoryImpl): PlaylistRepository

}