package com.jeanpaulo.musiclibrary.favorite.domain.di

import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class FavoriteDomainModule {

    @Binds
    abstract fun provideRepository(repository: FavoriteRepositoryImpl): FavoriteRepository

}