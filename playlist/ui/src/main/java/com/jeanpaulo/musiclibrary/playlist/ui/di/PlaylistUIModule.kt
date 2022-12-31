package com.jeanpaulo.musiclibrary.playlist.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.playlist.domain.*
import com.jeanpaulo.musiclibrary.playlist.domain.di.PlaylistDomainModule
import com.jeanpaulo.musiclibrary.playlist.ui.fragments.PlaylistCreateFragment
import com.jeanpaulo.musiclibrary.playlist.ui.fragments.PlaylistDetailFragment
import com.jeanpaulo.musiclibrary.playlist.ui.fragments.PlaylistFragment
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDetailViewModel
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        PlaylistUIModule::class,
        PlaylistDomainModule::class
    ]
)
abstract class PlaylistModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindPlaylistFragment(): PlaylistFragment


    @ContributesAndroidInjector
    abstract fun bindPlaylistDetailFragment(): PlaylistDetailFragment


    @ContributesAndroidInjector
    abstract fun bindPlaylistCreateFragment(): PlaylistCreateFragment
}

@Module
abstract class PlaylistUIModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistViewModel::class)
    abstract fun providePlaylistViewModel(viewModel: PlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistDetailViewModel::class)
    abstract fun providePlaylistDetailViewModel(viewModel: PlaylistDetailViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(PlaylistCreateViewModel::class)
//    abstract fun providePlaylistCreateViewModel(viewModel: PlaylistCreateViewModel): ViewModel

    @Binds
    abstract fun providePlaylistCreateInteractor(interactor: PlaylistCreateInteractorImpl): PlaylistCreateInteractor

    @Binds
    abstract fun providePlaylistDetailInteractor(interactor: PlaylistDetailInteractorImpl): PlaylistDetailInteractor

    @Binds
    abstract fun providePlaylistInteractor(interactor: PlaylistInteractorImpl): PlaylistInteractor


}