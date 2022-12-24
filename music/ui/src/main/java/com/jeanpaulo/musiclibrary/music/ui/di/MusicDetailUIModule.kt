package com.jeanpaulo.musiclibrary.music.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.commons.extensions.requireParcelable
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.MusicDetailInteractor
import com.jeanpaulo.musiclibrary.favorite.domain.MusicDetailInteractorImpl
import com.jeanpaulo.musiclibrary.music.ui.MusicDetailActivity
import com.jeanpaulo.musiclibrary.music.ui.MusicDetailFragment
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.MusicDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [MusicDetailModule::class]
)
abstract class MusicDetailModuleBuilder {
    @ContributesAndroidInjector
    abstract fun bindMusicDetailFragment(): MusicDetailFragment

    @ContributesAndroidInjector
    abstract fun bindMusicDetailActivity(): MusicDetailActivity
}

@Module
abstract class MusicDetailModule {

    @Binds
    @IntoMap
    @ViewModelKey(MusicDetailViewModel::class)
    abstract fun provideViewModel(viewModel: MusicDetailViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: MusicDetailInteractorImpl): MusicDetailInteractor

    @Module
    companion object {

        @JvmStatic
        @Provides
        @SimpleMusicUI
        fun provideMusic(activity: MusicDetailActivity): SimpleMusicDetailUIModel = activity.requireParcelable(MusicDetailActivity.MUSIC_PARAM)

    }

//    @Binds
//    abstract fun providesInteractor(interactor: MusicDetailInteractor): MusicDetailInteractor
}