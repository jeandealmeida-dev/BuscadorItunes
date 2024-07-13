package com.jeanpaulo.musiclibrary.app.di

import android.app.Application
import com.jeanpaulo.musiclibrary.app.CustomApplication
import com.jeanpaulo.musiclibrary.commons.di.modules.CommonsNetworkModule
import com.jeanpaulo.musiclibrary.core.di.NetworkModule
import com.jeanpaulo.musiclibrary.commons.di.modules.ApplicationModule
import com.jeanpaulo.musiclibrary.core.di.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        MainModule::class,
        ApplicationModule::class,
        NetworkModule::class,
        CommonsNetworkModule::class,
        DatabaseModule::class,
    ]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(application: CustomApplication)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}