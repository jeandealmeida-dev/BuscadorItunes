package com.jeanpaulo.buscador_itunes.app.di.modules

import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.buscador_itunes.app.MainActivity
import com.jeanpaulo.buscador_itunes.app.music.presentation.MusicActivity
import com.jeanpaulo.buscador_itunes.app.music_detail.di.MusicDetailModuleBuilder
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MusicDetailModuleBuilder::class])
    abstract fun bindMusicDetailActivity(): MusicDetailActivity

    @ContributesAndroidInjector(modules = [MusicModuleBuilder::class])
    abstract fun bindMusicActivity(): MusicActivity
}

@Module
class MainModule {
    @Provides
    fun provideAppCompatActivity(activity: MainActivity): AppCompatActivity = activity
}