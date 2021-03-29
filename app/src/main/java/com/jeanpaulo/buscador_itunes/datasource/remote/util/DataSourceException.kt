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

    override fun toString(): String {
        return when(knownNetworkError){
            Error.TIMEOUT_EXCEPTION-> "TIMEOUT_EXCEPTION"
            Error.NO_INTERNET_EXCEPTION-> "NO_INTERNET_EXCEPTION"
            Error.NULL_EXCEPTION-> "NULL_EXCEPTION"
            Error.UNKNOWN_EXCEPTION-> "UNKNOWN_EXCEPTION"
        }
    }
}
