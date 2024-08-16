package com.jeanpaulo.musiclibrary.commons.view

sealed class ViewState<out T> {
    data object Loading : ViewState<Nothing>()
    data object Error : ViewState<Nothing>()
    data object Empty : ViewState<Nothing>()
    class Success<out T>(val data: T) : ViewState<T>()
}