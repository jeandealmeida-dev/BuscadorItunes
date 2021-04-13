package com.jeanpaulo.buscador_itunes.view.favorite

import androidx.lifecycle.*
import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val dataSource: IDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _musicList: MutableLiveData<List<Music>> = MutableLiveData()
    val musicList: LiveData<List<Music>> = _musicList

    private val _openPlaylistEvent = MutableLiveData<Event<String>>()
    val openPlaylistEvent: LiveData<Event<String>> = _openPlaylistEvent

    private val _newPlaylistEvent = MutableLiveData<Event<Unit>>()
    val newPlaylistEvent: LiveData<Event<Unit>> = _newPlaylistEvent

    private val _deletePlaylistEvent = MutableLiveData<Event<Unit>>()
    val deletePlaylistEvent: LiveData<Event<Unit>> = _deletePlaylistEvent

    fun getFavoritePlaylist() {
        getFavoriteList()
    }

    private fun getFavoriteList() {
        GlobalScope.launch {
            setNetworkState(NetworkState.LOADING)

            //delay para dar tempo de carregar toda a animacao
            delay(200L)

            val response = dataSource.getFavoriteMusics()
            if (response is Result.Success) {
                val music = response.data
                _musicList.postValue(music)
                setNetworkState(NetworkState.DONE)
            } else {
                setNetworkState(NetworkState.ERROR)
            }
        }
    }

    //SETUP SNACKBAR

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    fun showSnackbarMessage(message: String) {
        //_snackbarText.value = Event(message)
    }

    fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    fun setNetworkState(networkState: NetworkState) {
        _queryState.postValue(networkState)
    }

    fun refresh() {
        getFavoriteList()
    }

    fun removeMusicFromFavorite(trackId: Long) {
        GlobalScope.launch {
            setNetworkState(NetworkState.LOADING)
            val response = dataSource.removeMusicFromFavorites(trackId)
            if (response is Result.Success) {
                setNetworkState(if (response.data) NetworkState.DONE else NetworkState.ERROR)
            } else {
                setNetworkState(NetworkState.ERROR)
            }
        }
    }

    //SETUP NETWORKSTATE

    private var _errorLoading: MutableLiveData<DataSourceException?> =
        MutableLiveData()
    val errorLoading: LiveData<DataSourceException?> = _errorLoading

    private var _queryState = MutableLiveData<NetworkState>()

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(musicList) {
        it.isEmpty()
    }

    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        _queryState
    ) { state ->
        when (state) {
            NetworkState.LOADING -> {
                //as this function is offline we dont need to show progress to the user
                MutableLiveData<Boolean>(false)
            }

            NetworkState.DONE -> {
                _errorLoading.postValue(null)
                MutableLiveData<Boolean>(false)
            }

            NetworkState.ERROR -> {
                _errorLoading.postValue(state.exception)
                MutableLiveData<Boolean>(false)
            }
        }
    }
}