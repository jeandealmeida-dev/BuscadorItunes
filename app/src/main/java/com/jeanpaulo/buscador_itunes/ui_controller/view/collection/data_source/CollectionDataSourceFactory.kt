package com.jeanpaulo.buscador_itunes.ui_controller.view.collection.data_source

import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.buscador_itunes.model.Collection
import com.jeanpaulo.buscador_itunes.util.CustomCallback
import io.reactivex.disposables.CompositeDisposable

class CollectionDataSourceFactory (
    private val collectionId: Long,
    private val service: ItunesService,
    private val composite: CompositeDisposable,
    private val callback: CustomCallback<Collection>
) {
    val dataSource = MutableLiveData<CollectionDataSource>()

    fun run(){

        val newsDataSource =
            CollectionDataSource(
                service,
                composite,
                callback
            )
        dataSource.postValue(newsDataSource)
        newsDataSource.run(collectionId)
    }

    fun retry() {

    }

}