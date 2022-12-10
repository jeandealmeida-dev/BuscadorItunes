package com.jeanpaulo.musiclibrary.commons.di.modules

import android.app.Application
import android.content.ContentResolver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Provides
    fun provideContentResolver(application: Application): ContentResolver = application.contentResolver

    @Provides
    @Singleton
    fun providesPackageManager(application: Application) = application.packageManager
}