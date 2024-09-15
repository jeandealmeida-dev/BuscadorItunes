package com.jeanpaulo.musiclibrary.commons.di.modules

import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Named

@Module
class CommonsNetworkModule {

    @Provides
    @IOScheduler
    fun provideIOSchedulers(): Scheduler = Schedulers.io()

    @Provides
    @MainScheduler
    fun provideMainSchedulers(): Scheduler = AndroidSchedulers.mainThread()
}