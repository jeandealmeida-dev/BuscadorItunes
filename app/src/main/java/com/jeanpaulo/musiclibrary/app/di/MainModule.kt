package com.jeanpaulo.musiclibrary.app.di

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.app.MainActivity
import com.jeanpaulo.musiclibrary.app.MainViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.favorite.ui.di.FavoriteModuleBuilder
import com.jeanpaulo.musiclibrary.music.ui.v1.di.MusicModuleBuilder as MusicModuleBuilderV1
import com.jeanpaulo.musiclibrary.music.ui.v2.di.MusicModuleBuilder as MusicModuleBuilderV2
import com.jeanpaulo.musiclibrary.music.ui.v1.view.MusicDetailActivity as MusicDetailActivityV1
import com.jeanpaulo.musiclibrary.music.ui.v2.view.MusicDetailActivity as MusicDetailActivityV2
import com.jeanpaulo.musiclibrary.playlist.ui.di.PlaylistModuleBuilder
import com.jeanpaulo.musiclibrary.search.ui.di.SearchModuleBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MainModule {

    @ContributesAndroidInjector(
        modules = [
            SearchModuleBuilder::class,
            FavoriteModuleBuilder::class,
            PlaylistModuleBuilder::class,
        ]
    )
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(
        modules = [
            MusicModuleBuilderV1::class,
            FavoriteModuleBuilder::class,
        ]
    )
    internal abstract fun bindMusicDetailActivity(): MusicDetailActivityV1


    @ContributesAndroidInjector(
        modules = [
            MusicModuleBuilderV2::class,
            FavoriteModuleBuilder::class,
        ]
    )
    internal abstract fun bindMusicDetailActivityV2(): MusicDetailActivityV2

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun provideViewModel(viewModel: MainViewModel): ViewModel

    companion object {
        @Provides
        fun provideAppCompatActivity(activity: MainActivity): AppCompatActivity = activity
    }
}