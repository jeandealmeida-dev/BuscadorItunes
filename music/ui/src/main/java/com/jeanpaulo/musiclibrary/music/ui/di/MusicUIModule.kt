package com.jeanpaulo.musiclibrary.music.ui.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.musiclibrary.commons.di.ViewModelKey
import com.jeanpaulo.musiclibrary.commons.extensions.requireBoolean
import com.jeanpaulo.musiclibrary.commons.extensions.requireParcelable
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.domain.MusicInteractor
import com.jeanpaulo.musiclibrary.music.domain.MusicInteractorImpl
import com.jeanpaulo.musiclibrary.music.domain.di.MusicDomainModule
import com.jeanpaulo.musiclibrary.music.ui.view.MusicDetailActivity
import com.jeanpaulo.musiclibrary.music.ui.MusicDetailViewModel
import com.jeanpaulo.musiclibrary.music.ui.view.MusicDetailFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module(
    includes = [
        MusicUIModule::class,
        MusicDomainModule::class,
    ]
)
abstract class MusicModuleBuilder {

    @ContributesAndroidInjector
    abstract fun bindMusicDetailFragment(): MusicDetailFragment

}

@Module
abstract class MusicUIModule {

    @Binds
    @IntoMap
    @ViewModelKey(MusicDetailViewModel::class)
    abstract fun provideViewModel(viewModel: MusicDetailViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: MusicInteractorImpl): MusicInteractor

    @Module
    companion object {

        @JvmStatic
        @Provides
        @SimpleMusicUI
        fun provideMusic(activity: MusicDetailActivity): SimpleMusicDetailUIModel =
            activity.requireParcelable(MusicDetailActivity.MUSIC_PARAM)


        @JvmStatic
        @Provides
        @FromRemote
        fun provideFromRemote(activity: MusicDetailActivity): Boolean =
            activity.requireBoolean(MusicDetailActivity.FROM_REMOTE_PARAM)

    }

//    @Binds
//    abstract fun providesInteractor(interactor: MusicDetailInteractor): MusicDetailInteractor
}