package com.jeanpaulo.buscador_itunes.repository.remote.paged_seach

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource
import io.reactivex.disposables.CompositeDisposable


class PagedSearchDataSourceFactory(
    val repository: MusicRemoteDataSource,
    val executor: CompositeDisposable,
    private var searchKey: String
) : DataSource.Factory<Int, Music>() {

    private var myDataSourceMutableLiveData: MutableLiveData<PagedSearchDataSource> =
        MutableLiveData()
    private var dataSource: PagedSearchDataSource? = null

    override fun create(): DataSource<Int, Music> {
        //*notice: It's important that everytime a DataSource factory create() is invoked a new DataSource instance is created
        dataSource =
            PagedSearchDataSource(
                repository,
                executor,
                searchKey
            )
        myDataSourceMutableLiveData.postValue(dataSource)
        return dataSource!!
    }

    fun setSearchKey(searchKey: String) {
        this.searchKey = searchKey
    }

    fun getDataSourceMutableLiveData(): MutableLiveData<PagedSearchDataSource>? {
        return myDataSourceMutableLiveData
    }

    fun getMyDataSource(): PagedSearchDataSource? {
        return dataSource
    }


}