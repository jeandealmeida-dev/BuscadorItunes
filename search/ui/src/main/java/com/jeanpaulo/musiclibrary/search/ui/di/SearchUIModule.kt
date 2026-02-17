package com.jeanpaulo.musiclibrary.search.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.search.domain.di.SearchDomainModule
import com.jeanpaulo.musiclibrary.search.ui.SearchFragment
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        SearchUIModule::class,
        SearchDomainModule::class,
    ]
)
abstract class SearchModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindSearchFragment(): SearchFragment

}

@Module
abstract class SearchUIModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideViewModel(viewModel: SearchViewModel): ViewModel
}