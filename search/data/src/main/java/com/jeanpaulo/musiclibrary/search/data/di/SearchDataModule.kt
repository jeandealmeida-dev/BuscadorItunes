package com.jeanpaulo.musiclibrary.search.data.di

import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class SearchDataModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideService(retrofit: Retrofit): ItunesService = retrofit.create(
            ItunesService::class.java
        )
    }
}