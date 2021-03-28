package com.jeanpaulo.buscador_itunes.datasource.remote.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

class RetrofitServiceFactory {

    companion object {
        val BASE_URL = "https://itunes.apple.com"
        lateinit var retrofit: Retrofit
            private set
    }

    fun build() {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(
                MoshiConverterFactory
                    .create(
                        Moshi
                            .Builder()
                            //.add(CollectionAdapter())
                            .add(Date::class.java, Rfc3339DateJsonAdapter() )
                            .addLast(KotlinJsonAdapterFactory())
                            .build()
                    )
            )
            .build()
    }
}