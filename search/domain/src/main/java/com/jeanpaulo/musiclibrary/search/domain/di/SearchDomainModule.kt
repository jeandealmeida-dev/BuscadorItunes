package com.jeanpaulo.musiclibrary.search.domain.di

import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import com.jeanpaulo.musiclibrary.search.data.SearchRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class SearchDomainModule {

    @Binds
    abstract fun provideRepository(repository: SearchRepositoryImpl): SearchRepository
}