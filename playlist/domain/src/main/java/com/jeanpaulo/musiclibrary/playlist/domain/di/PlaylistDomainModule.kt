package com.jeanpaulo.musiclibrary.playlist.domain.di

import com.jeanpaulo.musiclibrary.playlist.data.*
import dagger.Binds
import dagger.Module

@Module
abstract class PlaylistDomainModule {

    @Binds
    abstract fun providePlaylistRepository(repository: PlaylistRepositoryImpl): PlaylistRepository

    @Binds
    abstract fun providePlaylistDetailRepository(repository: PlaylistDetailRepositoryImpl): PlaylistDetailRepository

    @Binds
    abstract fun providePlaylistCreateRepository(repository: PlaylistCreateRepositoryImpl): PlaylistCreateRepository
}