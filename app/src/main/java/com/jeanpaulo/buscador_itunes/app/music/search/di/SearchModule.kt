package com.jeanpaulo.buscador_itunes.app.music.search.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.buscador_itunes.app.music.search.data.SearchRepository
import com.jeanpaulo.buscador_itunes.app.music.search.data.SearchRepositoryImpl
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.SearchFragment
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(
    includes = [SearchModule::class]
)
abstract class SearchModuleBuilder {
    @ContributesAndroidInjector
    abstract fun bindSearchFragment(): SearchFragment
}

@Module
abstract class SearchModule {
    @Binds
    @IntoMap
    @com.jeanpaulo.musiclibrary.commons.di.ViewModelKey(SearchViewModel::class)
    abstract fun provideViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    abstract fun provideRepository(repository: SearchRepositoryImpl): SearchRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideService(retrofit: Retrofit): ItunesService = retrofit.create(
            ItunesService::class.java
        )
    }
}