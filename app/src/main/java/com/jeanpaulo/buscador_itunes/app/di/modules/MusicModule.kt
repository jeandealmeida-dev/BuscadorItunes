package com.jeanpaulo.buscador_itunes.app.di.modules

import androidx.lifecycle.ViewModel
import com.jeanpaulo.buscador_itunes.app.music.presentation.viewmodel.MusicViewModel
import com.jeanpaulo.buscador_itunes.app.music.search.di.SearchModuleBuilder
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.favorite.di.FavoriteModuleBuilder
import com.jeanpaulo.musiclibrary.playlist.di.PlaylistModuleBuilder
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module(
    includes = [
        MusicModule::class,
        SearchModuleBuilder::class,
        FavoriteModuleBuilder::class,
        PlaylistModuleBuilder::class
    ]
)
abstract class MusicModuleBuilder {
}

@Module
abstract class MusicModule {

    @Binds
    @IntoMap
    @ViewModelKey(MusicViewModel::class)
    abstract fun provideViewModel(viewModel: MusicViewModel): ViewModel

}