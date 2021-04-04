package com.jeanpaulo.buscador_itunes.view.music.music_search.music_detail

import androidx.lifecycle.*
import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.Event
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MusicDetailViewModel(
    private val dataSource: IDataSource,
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
        return savedStateHandle.get(MUSIC_ID_PARAM)
    }

    private fun getMusicDetail(musicId: Long) {
        GlobalScope.launch {
            setNetworkState(NetworkState.LOADING)

            //delay para dar tempo de carregar toda a animacao
            delay(600L)

            val response = dataSource.lookup(musicId, SearchParams.SONG_MEDIA_TYPE)
            if (response is Result.Success) {
                val music = response.data
                _music.postValue(music)
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
        _networkState.postValue(networkState)
    }

    fun setMusicId(musicId: Long) {
        savedStateHandle.set(MUSIC_ID_PARAM, musicId)
        getMusicDetail(musicId)
    }

    fun refresh() {
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