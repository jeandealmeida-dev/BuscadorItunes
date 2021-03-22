package com.jeanpaulo.buscador_itunes.datasource.remote.service

import com.jeanpaulo.buscador_itunes.view.adapter.CollectionAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ItunesServiceFactory {

    companion object {
        val BASE_URL = "https://itunes.apple.com"
        lateinit var retrofit : Retrofit
            private set
    }

    fun build(){
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory
                .create(
                    Moshi
                    .Builder()
                    .add(CollectionAdapter())
                    .addLast(KotlinJsonAdapterFactory())
                    .build()
                )
            ).build()
    }

}