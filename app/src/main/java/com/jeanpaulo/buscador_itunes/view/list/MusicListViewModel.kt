package com.jeanpaulo.buscador_itunes.view.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.buscador_itunes.CustomApplication
import com.jeanpaulo.buscador_itunes.view.data_source.MusicDataSource
import com.jeanpaulo.buscador_itunes.view.data_source.MusicDataSourceFactory
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.NetworkState
import com.jeanpaulo.buscador_itunes.service.ItunesService
import io.reactivex.disposables.CompositeDisposable

class MusicListViewModel : ViewModel() {

    var newsList: LiveData<PagedList<Music>>
    private val compositeDisposable = CompositeDisposable()
    private val pageSize = 5
    private val musicDataSourceFactory: MusicDataSourceFactory

    init {
        val service = CustomApplication.retrofit.create(ItunesService::class.java)

        musicDataSourceFactory =
            MusicDataSourceFactory(
                compositeDisposable,
                service
            )
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        newsList = LivePagedListBuilder<Int, Music>(musicDataSourceFactory, config).build()
    }


    fun getState(): LiveData<NetworkState> = Transformations.switchMap<MusicDataSource,
            NetworkState>(musicDataSourceFactory.newsDataSourceLiveData, MusicDataSource::state)

    fun retry() {
        musicDataSourceFactory.newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return newsList.value?.isEmpty() ?: true
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}