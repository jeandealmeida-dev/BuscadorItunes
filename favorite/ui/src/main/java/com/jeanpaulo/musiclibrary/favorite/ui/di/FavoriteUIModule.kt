package com.jeanpaulo.musiclibrary.favorite.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractorImpl
import com.jeanpaulo.musiclibrary.favorite.domain.di.FavoriteDomainModule
import com.jeanpaulo.musiclibrary.favorite.ui.FavoriteViewModel
import com.jeanpaulo.musiclibrary.favorite.ui.view.FavoriteFragment
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        FavoriteUIModule::class,
        FavoriteDomainModule::class
    ]
)
abstract class FavoriteModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): FavoriteFragment
}

@Module
abstract class FavoriteUIModule {

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel::class)
    abstract fun provideViewModel(viewModel: FavoriteViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: FavoriteInteractorImpl): FavoriteInteractor
}