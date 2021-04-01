package com.jeanpaulo.buscador_itunes.view.fragment.playlist_list

import androidx.lifecycle.*
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.Event
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val dataSource: MusicDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _playlistList: MutableLiveData<List<Playlist>> = MutableLiveData()
    val playlistList: LiveData<List<Playlist>> = _playlistList

    private val _openPlaylistEvent = MutableLiveData<Event<String>>()
    val openPlaylistEvent: LiveData<Event<String>> = _openPlaylistEvent

    private val _newPlaylistEvent = MutableLiveData<Event<Unit>>()
    val newPlaylistEvent: LiveData<Event<Unit>> = _newPlaylistEvent

    fun searchPlaylist(){
        getPlaylistList()
    }

    private fun getPlaylistList() {
        GlobalScope.launch {
            setNetworkState(NetworkState.LOADING)

            //delay para dar tempo de carregar toda a animacao
            delay(200L)

            val response = dataSource.getPlaylists()
            if (response is Result.Success) {
                val music = response.data
                _playlistList.postValue(music)
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
        getPlaylistList()
    }

    //SETUP NETWORKSTATE

    private var _errorLoading: MutableLiveData<DataSourceException?> =
        MutableLiveData()
    val errorLoading: LiveData<DataSourceException?> = _errorLoading

    private var _queryState = MutableLiveData<NetworkState>()

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(playlistList) {
        it.isEmpty()
    }

    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        _queryState
    ) { state ->
        when (state) {
            NetworkState.LOADING -> {
                MutableLiveData<Boolean>(true)
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