package com.jeanpaulo.buscador_itunes.model.util

import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import java.lang.Exception

enum class NetworkState(var exception: DataSourceException? = null) {
    DONE,
    LOADING,
    ERROR;
}
