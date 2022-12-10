package com.jeanpaulo.buscador_itunes.app.music_detail.di

import androidx.lifecycle.ViewModel
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.viewmodel.MusicDetailViewModel
import com.jeanpaulo.buscador_itunes.app.music_detail.domain.MusicDetailInteractor
import com.jeanpaulo.buscador_itunes.app.music_detail.domain.MusicDetailInteractorImpl
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailFragment
import com.jeanpaulo.musiclibrary.core.repository.remote.MusicDetailRepository
import com.jeanpaulo.musiclibrary.core.repository.remote.MusicDetailRepositoryImpl
import com.jeanpaulo.musiclibrary.core.repository.remote.ItunesService
import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.commons.extensions.requireParcelable
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import retrofit2.Retrofit

@Module(
    includes = [MusicDetailModule::class]
)
abstract class MusicDetailModuleBuilder {
    @ContributesAndroidInjector
    abstract fun bindMusicDetailFragment(): MusicDetailFragment
}

@Module
abstract class MusicDetailModule {

    @Binds
    @IntoMap
    @com.jeanpaulo.musiclibrary.commons.di.ViewModelKey(MusicDetailViewModel::class)
    abstract fun provideViewModel(viewModel: MusicDetailViewModel): ViewModel

    @Binds
    abstract fun provideInteractor(interactor: MusicDetailInteractorImpl): MusicDetailInteractor

    @Binds
    abstract fun provideRepository(repository: MusicDetailRepositoryImpl): MusicDetailRepository

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideService(retrofit: Retrofit): ItunesService =  retrofit.create(
            ItunesService::class.java)

        @JvmStatic
        @Provides
        @SimpleMusicUI
        fun provideMusic(activity: MusicDetailActivity): SimpleMusicDetailUIModel = activity.requireParcelable<SimpleMusicDetailUIModel>(
            MusicDetailActivity.MUSIC_PARAM)

    }

//    @Binds
//    abstract fun providesInteractor(interactor: MusicDetailInteractor): MusicDetailInteractor
}