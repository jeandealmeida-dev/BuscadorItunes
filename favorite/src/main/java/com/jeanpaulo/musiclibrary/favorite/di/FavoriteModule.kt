package com.jeanpaulo.musiclibrary.favorite.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepository
import com.jeanpaulo.musiclibrary.favorite.data.FavoriteRepositoryImpl
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractorImpl
import com.jeanpaulo.musiclibrary.favorite.presentation.view.FavoriteFragment
import com.jeanpaulo.musiclibrary.favorite.presentation.viewmodel.FavoriteViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [FavoriteModule::class]
)
abstract class FavoriteModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): FavoriteFragment
}

@Module
abstract class FavoriteModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun provideViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: FavoriteInteractorImpl): FavoriteInteractor

    @Binds
    abstract fun provideRepository(repository: FavoriteRepositoryImpl): FavoriteRepository
}