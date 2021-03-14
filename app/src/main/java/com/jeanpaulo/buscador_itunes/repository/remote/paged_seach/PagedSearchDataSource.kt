package com.jeanpaulo.buscador_itunes.repository.remote.paged_seach

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PagedSearchDataSource(
    private val repository: MusicRemoteDataSource,
    private val composite: CompositeDisposable,
    private val searchKey: String
) : PageKeyedDataSource<Int, Music>() {

    private var logger: Logger = LoggerFactory.getLogger(PagedSearchDataSource::class.java)
    val state: MutableLiveData<NetworkState> = MutableLiveData()

    private var retryCompletable: Completable? = null

    //IMPLEMENTS

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Music>
    ) {
        composite.add(
            repository.searchMusic(
                searchKey,
                SearchParams.SEARCH_MEDIA_TYPE,
                0,
                SearchParams.SEARCH_PAGE_SIZE
            )
                .subscribe(
                    { response ->
                        logger.info("onResult ->", response)
                        updateState(NetworkState.DONE)
                        callback.onResult(
                            response.result,
                            null,
                            SearchParams.SEARCH_PAGE_SIZE
                        )
                    },
                    {
                        logger.info("onError ->", it)
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadInitial(params, callback) })
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        updateState(NetworkState.LOADING)
        composite.add(
            repository.searchMusic(
                searchKey,
                SearchParams.SEARCH_MEDIA_TYPE,
                params.key,
                SearchParams.SEARCH_PAGE_SIZE
            )
                .subscribe(
                    { response ->
                        logger.info("onResult ->", response)
                        updateState(NetworkState.DONE)
                        callback.onResult(
                            response.result,
                            params.key + SearchParams.SEARCH_PAGE_SIZE
                        )
                    },
                    {
                        logger.info("onError ->", it)
                        updateState(NetworkState.ERROR)
                        setRetry(Action { loadAfter(params, callback) })
                    }
                )
        )
    }

    //OTHER METHODS

    fun updateState(networkState: NetworkState) {
        this.state.postValue(networkState)
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

    fun dispose(){
        if(composite != null)
            composite.dispose()
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {}
}
