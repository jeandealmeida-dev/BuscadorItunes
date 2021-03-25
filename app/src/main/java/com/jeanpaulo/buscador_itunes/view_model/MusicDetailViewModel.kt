package com.jeanpaulo.buscador_itunes.view_model

import androidx.lifecycle.*
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.Event
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import com.jeanpaulo.buscador_itunes.view.activity.TRACK_ID_PARAM
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MusicDetailViewModel(
    private val dataSource: MusicDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _music: MutableLiveData<Music> = MutableLiveData()
    val music: LiveData<Music> = _music

    init {
        searchMusic()
    }

    private fun searchMusic() {
        val music = getMusic()
        if (music != null)
            getMusicDetail(music)
    }

    private fun getMusic(): Long? {
        return savedStateHandle.get(TRACK_ID_PARAM)
    }

    private fun getMusicDetail(musicId: Long) {
        GlobalScope.launch {
            setNetworkState(NetworkState.LOADING)
            val response = dataSource.lookup(musicId, SearchParams.SONG_MEDIA_TYPE)
            if (response is Result.Success) {
                _music.postValue(response.data.result.get(0))
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

    fun reproducePreview() {
        //TODO Jean Reproduzir musica
    }

    fun setNetworkState(networkState: NetworkState) {
        _networkState.postValue(networkState)
    }

    fun setMusicId(musicId: Long) {
        savedStateHandle.set(TRACK_ID_PARAM, musicId)
        getMusicDetail(musicId)
    }

    fun retry() {
        searchMusic()
    }

    //SETUP NETWORKSTATE

    private var _errorLoading: MutableLiveData<DataSourceException?> =
        MutableLiveData()
    val errorLoading: LiveData<DataSourceException?> = _errorLoading

    private var _networkState = MutableLiveData<NetworkState>()

    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        _networkState
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