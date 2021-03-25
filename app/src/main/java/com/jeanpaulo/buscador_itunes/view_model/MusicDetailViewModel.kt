package com.jeanpaulo.buscador_itunes.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.model.Collection
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.util.CustomCallback
import com.jeanpaulo.buscador_itunes.ui_controller.view.collection.data_source.CollectionDataSource
import com.jeanpaulo.buscador_itunes.ui_controller.view.collection.data_source.CollectionDataSourceFactory
import io.reactivex.disposables.CompositeDisposable

class CollectionViewModel(
    private val dataSource: MusicDataSource,
    private val savedStateHandle: SavedStateHandle
) {

    private val compositeDisposable = CompositeDisposable()
    private var dataSource: CollectionDataSourceFactory? = null

    init {
        val service = null//CustomApplication.retrofit.create(ItunesService::class.java)

        dataSource =
            CollectionDataSourceFactory(
                collectionId,
                service!!,
                compositeDisposable,
                callback
            )
    }

    fun getCollection(){
        dataSource!!.run()
    }


    fun getState(): LiveData<NetworkState> = Transformations.switchMap<CollectionDataSource,
            NetworkState>(dataSource!!.dataSource, CollectionDataSource::state)

    fun retry() {
        dataSource!!.retry()
    }

    fun onClose() {
        compositeDisposable.dispose()
    }


}