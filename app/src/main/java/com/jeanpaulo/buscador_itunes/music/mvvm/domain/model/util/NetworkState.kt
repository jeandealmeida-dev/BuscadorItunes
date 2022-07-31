package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util

import com.jeanpaulo.buscador_itunes.util.DataSourceException

enum class NetworkState(var exception: DataSourceException? = null) {
    DONE,
    LOADING,
    ERROR;
}
