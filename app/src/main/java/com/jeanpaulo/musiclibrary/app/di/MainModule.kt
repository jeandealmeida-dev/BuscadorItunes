package com.jeanpaulo.musiclibrary.app.di

import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.musiclibrary.app.MainActivity
import com.jeanpaulo.musiclibrary.artist.ui.di.ArtistModuleBuilder
import com.jeanpaulo.musiclibrary.favorite.ui.di.FavoriteModuleBuilder
import com.jeanpaulo.musiclibrary.playlist.ui.di.PlaylistModuleBuilder
import com.jeanpaulo.musiclibrary.search.ui.di.SearchModuleBuilder
import com.jeanpaulo.musiclibrary.settings.ui.SettingsActivity
import com.jeanpaulo.musiclibrary.settings.ui.di.SettingsModuleBuilder
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainModule {

    @ContributesAndroidInjector(
        modules = [
            SearchModuleBuilder::class,
            FavoriteModuleBuilder::class,
            PlaylistModuleBuilder::class,
            ArtistModuleBuilder::class,
        ]
    )
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(
        modules = [
            SettingsModuleBuilder::class,
            PlaylistModuleBuilder::class,
        ]
    )
    internal abstract fun bindSettingsActivity(): SettingsActivity

    companion object {
        @Provides
        fun provideAppCompatActivity(activity: MainActivity): AppCompatActivity = activity
    }
}