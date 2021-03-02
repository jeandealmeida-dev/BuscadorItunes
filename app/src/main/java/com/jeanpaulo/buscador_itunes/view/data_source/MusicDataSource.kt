package com.jeanpaulo.buscador_itunes.view.data_source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.NetworkState
import com.jeanpaulo.buscador_itunes.service.ItunesService
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers

class MusicDataSource(
    private val service: ItunesService,
    private val composite: CompositeDisposable
) : PageKeyedDataSource<Int, Music>() {

    var state: MutableLiveData<NetworkState> = MutableLiveData()
    private var retryCompletable: Completable? = null

    private val COUNT_SEARCH_LIMIT = 5

    //IMPLEMENTS

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Music>
    ) {
        composite.add(
            service.searchMusic("Nirvana", "music", 0,COUNT_SEARCH_LIMIT)
                .subscribe(
                    { response ->
                        updateState(NetworkState.DONE)
                        callback.onResult(response.result,
                            null,
                            COUNT_SEARCH_LIMIT
                        )
                    },
                    {
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        updateState(NetworkState.LOADING)
        composite.add(
            service.searchMusic("Nivana", "music", params.key ,COUNT_SEARCH_LIMIT)
                .subscribe(
                    { response ->
                        updateState(NetworkState.DONE)
                        callback.onResult(response.result,
                            params.key + COUNT_SEARCH_LIMIT
                        )
                    },
                    {
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        TODO("Not yet implemented")
    }

    //OTHER METHODS

    private fun updateState(state: NetworkState) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            composite.add(retryCompletable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe())
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}
