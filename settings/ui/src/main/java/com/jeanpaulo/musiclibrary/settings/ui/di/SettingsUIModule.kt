package com.jeanpaulo.musiclibrary.settings.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractorImpl
import com.jeanpaulo.musiclibrary.playlist.domain.di.PlaylistDomainModule
import com.jeanpaulo.musiclibrary.settings.domain.SettingsInteractor
import com.jeanpaulo.musiclibrary.settings.domain.SettingsInteractorImpl
import com.jeanpaulo.musiclibrary.settings.domain.di.SettingsDomainModule
import com.jeanpaulo.musiclibrary.settings.ui.SettingsFragment
import com.jeanpaulo.musiclibrary.settings.ui.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        SettingsModule::class,
        SettingsDomainModule::class,
        PlaylistDomainModule::class,
    ]
)
abstract class SettingsModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment(): SettingsFragment
}

@Module
abstract class SettingsModule {

    @Binds
    abstract fun provideInteractor(interactor: SettingsInteractorImpl): SettingsInteractor

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun provideViewModel(viewModel: SettingsViewModel): ViewModel
}