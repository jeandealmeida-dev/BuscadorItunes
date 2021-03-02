package com.jeanpaulo.buscador_itunes.view.data_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.service.ItunesService
import com.jeanpaulo.buscador_itunes.view.data_source.MusicDataSource
import io.reactivex.disposables.CompositeDisposable

class MusicDataSourceFactory(
    private val composite: CompositeDisposable,
    private val service: ItunesService
) : DataSource.Factory<Int, Music>() {

    val newsDataSourceLiveData = MutableLiveData<MusicDataSource>()

    override fun create(): DataSource<Int, Music> {
        val newsDataSource =
            MusicDataSource(
                service,
                composite
            )
        newsDataSourceLiveData.postValue(newsDataSource)
        return newsDataSource
    }
}