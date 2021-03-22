package com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach

import androidx.paging.PageKeyedDataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class PagedSearchDataSource(
    private val searchTermUseCase: SearchTermUseCase,
    private val networkStateUpdate: (NetworkState) -> Unit,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : PageKeyedDataSource<Int, Music>() {

    private var logger: Logger = LoggerFactory.getLogger(PagedSearchDataSource::class.java)

    //IMPLEMENTS

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Music>
    ) {
        runBlocking {
            updateState(NetworkState.LOADING)
            val response = searchTermUseCase.execute(
                0,
                SearchParams.SEARCH_PAGE_SIZE
            )
            if (response is Result.Success) {
                callback.onResult(
                    response.data.result,
                    null,
                    SearchParams.SEARCH_PAGE_SIZE
                )
                delay(100L)
                updateState(NetworkState.DONE)
            } else {
                updateState(NetworkState.ERROR)
                setRetry(Action { loadInitial(params, callback) })
            }
        }
    }


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {
        runBlocking {
            val response = searchTermUseCase.execute(
                params.key,
                SearchParams.SEARCH_PAGE_SIZE
            )
            if (response is Result.Success) {
                callback.onResult(
                    response.data.result,
                    params.key + SearchParams.SEARCH_PAGE_SIZE
                )
                delay(100L)
            } else {
                logger.info("onError ->", "error")
                setRetry(Action { loadAfter(params, callback) })
            }
        }
    }

    //OTHER METHODS

    fun updateState(networkState: NetworkState) {
        networkStateUpdate(networkState)
    }

    private var retryCompletable: Completable? = null

    fun retry() {
        if (retryCompletable != null) {
            GlobalScope.async {
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()

            }
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Music>) {}
}
