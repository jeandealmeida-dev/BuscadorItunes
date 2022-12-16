package com.jeanpaulo.musiclibrary.search.domain.di

import com.jeanpaulo.musiclibrary.search.data.SearchRepository
import com.jeanpaulo.musiclibrary.search.data.SearchRepositoryImpl
import com.jeanpaulo.musiclibrary.search.data.di.SearchDataModule
import dagger.Binds
import dagger.Module


@Module(
    includes = [SearchDataModule::class]
)
abstract class SearchDomainModule {
    @Binds
    abstract fun provideRepository(repository: SearchRepositoryImpl): SearchRepository
}