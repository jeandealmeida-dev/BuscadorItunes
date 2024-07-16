package com.jeanpaulo.musiclibrary.app

import android.content.Context
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.jeanpaulo.musiclibrary.app.di.AppComponent
import com.jeanpaulo.musiclibrary.app.di.DaggerAppComponent
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import com.jeanpaulo.musiclibrary.settings.ui.applyTheme
import com.jeanpaulo.musiclibrary.settings.ui.getPreferenceTheme

@Component(modules = [AndroidInjectionModule::class])
interface ApplicationComponent : AndroidInjector<CustomApplication>

class CustomApplication : DaggerApplication() {

    private lateinit var appComponent: AppComponent

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        appComponent = DaggerAppComponent
            .builder()
            .application(this)
            .build()
            .also {
                it.inject(this)
            }
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        loadPreferenceTheme()
        //DEBUG CODE
        //if (BuildConfig.DEBUG) {
        //Timber.plant(Timber.DebugTree());
        //}
    }

    private fun loadPreferenceTheme() {
        val themePreference = getPreferenceTheme(resources)
        applyTheme(resources, themePreference)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}