package com.jeanpaulo.musiclibrary.playlist.create.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.core.repository.database.dao.PlaylistDao
import com.jeanpaulo.musiclibrary.core.repository.database.MusicDatabase
import com.jeanpaulo.musiclibrary.playlist.create.data.PlaylistCreateRepository
import com.jeanpaulo.musiclibrary.playlist.create.data.PlaylistCreateRepositoryImpl
import com.jeanpaulo.musiclibrary.playlist.create.domain.PlaylistCreateInteractor
import com.jeanpaulo.musiclibrary.playlist.create.domain.PlaylistCreateInteractorImpl
import com.jeanpaulo.musiclibrary.playlist.create.view.PlaylistCreateFragment
import com.jeanpaulo.musiclibrary.playlist.create.view.PlaylistCreateViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [PlaylistCreateModule::class]
)
abstract class PlaylistCreateModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): PlaylistCreateFragment
}

@Module
abstract class PlaylistCreateModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistCreateViewModel::class)
    abstract fun provideViewModel(viewModel: PlaylistCreateViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: PlaylistCreateInteractorImpl): PlaylistCreateInteractor

    @Binds
    abstract fun provideRepository(repository: PlaylistCreateRepositoryImpl): PlaylistCreateRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun providePlaylistDao(musicDatabase: MusicDatabase): PlaylistDao =  musicDatabase.playlistDao()

    }
}