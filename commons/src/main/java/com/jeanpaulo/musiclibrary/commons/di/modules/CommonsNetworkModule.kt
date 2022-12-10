package com.jeanpaulo.musiclibrary.commons.di.modules

import dagger.Module
import dagger.Provides
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Named

@Module
class CommonsNetworkModule {
    @Provides
    @Named("IOScheduler")
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @Provides
    @Named("MainScheduler")
    fun provideMainScheduler(): Scheduler = AndroidSchedulers.mainThread()
}