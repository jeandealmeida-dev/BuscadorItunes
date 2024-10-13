package com.jeanpaulo.musiclibrary.artist.domain.di

import com.jeanpaulo.musiclibrary.artist.data.ArtistRepository
import com.jeanpaulo.musiclibrary.artist.data.ArtistRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ArtistDomainModule {

    @Binds
    abstract fun provideRepository(repository: ArtistRepositoryImpl): ArtistRepository
}