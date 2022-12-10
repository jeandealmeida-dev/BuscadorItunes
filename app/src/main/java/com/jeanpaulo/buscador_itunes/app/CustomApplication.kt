package com.jeanpaulo.buscador_itunes.app

import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jeanpaulo.buscador_itunes.BuildConfig
import com.jeanpaulo.buscador_itunes.app.di.AppComponent
import com.jeanpaulo.buscador_itunes.app.di.DaggerAppComponent
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

@Component(modules = [AndroidInjectionModule::class])
interface ApplicationComponent : AndroidInjector<CustomApplication>

class CustomApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()

        appComponent.inject(this)

        return appComponent
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        //DEBUG CODE
        if (BuildConfig.DEBUG) {
            //Timber.plant(Timber.DebugTree());
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun isDebuging() = BuildConfig.DEBUG

}