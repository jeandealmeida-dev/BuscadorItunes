package com.jeanpaulo.buscador_itunes.datasource.remote.util

class DataSourceException(
    val knownNetworkError: Error,
    val exception: String
) : Exception() {

    enum class Error {
        TIMEOUT_EXCEPTION,
        NO_INTERNET_EXCEPTION,
        NULL_EXCEPTION,
        UNKNOWN_EXCEPTION
    }
}
