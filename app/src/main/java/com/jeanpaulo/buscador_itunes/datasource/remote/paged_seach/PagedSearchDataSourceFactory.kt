package com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach

import androidx.paging.DataSource
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.datasource.IDataSource


class PagedSearchDataSourceFactory(
    val dataSource: IDataSource,
    private val searchMediaType: String,
    private val searchKey: String,
    private val networkStateUpdate: (NetworkState) -> Unit
) : DataSource.Factory<Int, Music>() {

    override fun create(): DataSource<Int, Music> {
        //*notice: It's important that everytime a DataSource factory create() is invoked a new DataSource instance is created
        val searchTermUseCase =
            SearchTermUseCase(dataSource, searchKey, searchMediaType)


        var _dataSource =
            PagedSearchDataSource(
                searchTermUseCase,
                networkStateUpdate
            )

        return _dataSource
    }


}