package com.jeanpaulo.buscador_itunes

import android.app.Application
import android.content.res.Configuration
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class CustomApplication : Application() {

    //Singleton reference application
    //TODO Singleton class
    companion object {
        val BASE_URL = "https://itunes.apple.com"
        lateinit var instance : CustomApplication
            private set
        lateinit var retrofit : Retrofit
            private set
    }


    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!
    override fun onCreate() {
        super.onCreate()

        //Initialize static variables
        instance = this
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory
                .create(Moshi
                    .Builder()
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                )
            ).build()


        //DEBUG CODE
        if (BuildConfig.DEBUG) {
            //Timber.plant(Timber.DebugTree());
        }
    }

    // Called by the system when the device configuration changes while your component is running.
    // Overriding this method is totally optional!
    override fun onConfigurationChanged ( newConfig : Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    // This is called when the overall system is running low on memory,
    // and would like actively running processes to tighten their belts.
    // Overriding this method is totally optional!
    override fun onLowMemory() {
        super.onLowMemory()
    }
}