package com.jeanpaulo.musiclibrary.artist.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.artist.domain.ArtistInteractor
import com.jeanpaulo.musiclibrary.artist.domain.ArtistInteractorImpl
import com.jeanpaulo.musiclibrary.artist.domain.di.ArtistDomainModule
import com.jeanpaulo.musiclibrary.artist.ui.view.ArtistFragment
import com.jeanpaulo.musiclibrary.artist.ui.view.ArtistViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        ArtistUIModule::class,
        ArtistDomainModule::class
    ]
)
abstract class ArtistModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindFragment(): ArtistFragment
}

@Module
abstract class ArtistUIModule {

    @Binds
    @IntoMap
    @ViewModelKey(ArtistViewModel::class)
    abstract fun provideViewModel(viewModel: ArtistViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: ArtistInteractorImpl): ArtistInteractor
}