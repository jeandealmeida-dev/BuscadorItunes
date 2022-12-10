package com.jeanpaulo.musiclibrary.playlist.detail.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.core.repository.database.dao.PlaylistDao
import com.jeanpaulo.musiclibrary.core.repository.database.MusicDatabase
import com.jeanpaulo.musiclibrary.playlist.detail.data.PlaylistDetailRepository
import com.jeanpaulo.musiclibrary.playlist.detail.data.PlaylistDetailRepositoryImpl
import com.jeanpaulo.musiclibrary.playlist.detail.domain.PlaylistDetailInteractor
import com.jeanpaulo.musiclibrary.playlist.detail.domain.PlaylistDetailInteractorImpl
import com.jeanpaulo.musiclibrary.playlist.detail.view.PlaylistDetailFragment
import com.jeanpaulo.musiclibrary.playlist.detail.view.PlaylistDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [PlaylistDetailModule::class]
)
abstract class PlaylistDetailModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): PlaylistDetailFragment
}

@Module
abstract class PlaylistDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistDetailViewModel::class)
    abstract fun provideViewModel(viewModel: PlaylistDetailViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: PlaylistDetailInteractorImpl): PlaylistDetailInteractor

    @Binds
    abstract fun provideRepository(repository: PlaylistDetailRepositoryImpl): PlaylistDetailRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun providePlaylistDao(musicDatabase: MusicDatabase): PlaylistDao =  musicDatabase.playlistDao()

    }
}