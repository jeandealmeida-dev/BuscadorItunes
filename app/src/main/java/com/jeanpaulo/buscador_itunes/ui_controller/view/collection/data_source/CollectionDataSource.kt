package com.jeanpaulo.buscador_itunes.ui_controller.view.collection.data_source

import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.buscador_itunes.model.Collection
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.ItunesResponse2
import com.jeanpaulo.buscador_itunes.util.CustomCallback
import com.jeanpaulo.buscador_itunes.util.params.CollectionParams
import com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach.PagedSearchDataSource
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CollectionDataSource(
    val service: MusicDataSource,
    val composite: CompositeDisposable,
    val callback: CustomCallback<Collection>
) : SingleObserver<ItunesResponse2> {

    val logger: Logger = LoggerFactory.getLogger(PagedSearchDataSource::class.java)

    var state: MutableLiveData<NetworkState> = MutableLiveData()
    private var retryCompletable: Completable? = null

    fun run(
        collectionId: Long
    ) {

        /*Observable.create<Collection> { observer ->
            service.getCollection(
                collectionId,
                SearchParams.SEARCH_ENTITY
            ).subscribe(
                { response ->
                    logger.info("onResult ->", response)
                    updateState(NetworkState.DONE)
                    //callback.onResult(
                    //    response.result
                    //)
                },
                {
                    logger.info("onError ->", it)
                    updateState(NetworkState.ERROR)
                    //setRetry(Action { loadInitial() })
                })
        }.subscribeOn(Schedulers.io())*/
    }

    //OTHER METHODS

    private fun updateState(state: NetworkState) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            composite.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    override fun onSubscribe(d: Disposable) {
        logger.info("onSubscribe ->", d)
    }

    override fun onError(e: Throwable) {
        logger.debug("onError ->", e)
        updateState(NetworkState.ERROR)
        //setRetry(Action { run(collectionId) })
    }

    override fun onSuccess(itunes: ItunesResponse2) {
        logger.info("onResult ->", itunes)
        updateState(NetworkState.DONE)
        callback.onResult(
            Collection(
                CollectionParams.EG_COLLECTION,
                CollectionParams.EG_TERM
            )
        )
    }
}