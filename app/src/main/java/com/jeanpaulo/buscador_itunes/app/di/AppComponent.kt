package com.jeanpaulo.buscador_itunes.app.di

import android.app.Application
import com.jeanpaulo.musiclibrary.commons.di.modules.CommonsNetworkModule
import com.jeanpaulo.buscador_itunes.app.CustomApplication
import com.jeanpaulo.buscador_itunes.app.di.modules.MainActivityModule
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
//        StockUiModule::class,
//        SegmentQualifiersFakeModule::class,
        AndroidSupportInjectionModule::class,
//        FakeLoginModule::class,
        MainActivityModule::class,
        ApplicationModule::class,
        NetworkModule::class,
        CommonsNetworkModule::class,
        DatabaseModule::class,
//        ExchangeModule::class,
//        NetworkModule::class,
//        ApplicationModule::class,
//        AllCommonsModule::class
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