package com.jeanpaulo.buscador_itunes.view_model

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach.PagedSearchDataSourceFactory
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.util.Event


class SearchViewModel(
    private val dataSource: MusicDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText


    var filterTextAll = MutableLiveData<String>()
    private lateinit var dataSourceFactory: PagedSearchDataSourceFactory

    private var _musicList: LiveData<PagedList<Music>>? =
        Transformations.switchMap<String, PagedList<Music>>(
            filterTextAll
        ) { input ->

            _networkState.postValue(NetworkState.LOADING)

            dataSourceFactory =
                PagedSearchDataSourceFactory(
                    dataSource = dataSource,
                    searchMediaType = SearchParams.MUSIC_MEDIA_TYPE,
                    searchKey = input,
                    networkStateUpdate = {
                        _networkState.postValue(it)
                    }
                )

            //update
            saveLastSearchTerm(input)

            LivePagedListBuilder(
                dataSourceFactory,
                PagedList.Config.Builder()
                    .setPageSize(SearchParams.SEARCH_PAGE_SIZE)
                    .setInitialLoadSizeHint(SearchParams.SEARCH_PAGE_SIZE * 2)
                    .setEnablePlaceholders(false)
                    .build()
            ).build()

        }

    private var _networkState: MutableLiveData<NetworkState> = MutableLiveData<NetworkState>()

    val musicList: LiveData<PagedList<Music>>? = _musicList

    init {
        search(getSavedSearch())
    }

    fun saveLastSearchTerm(term: String) {
        savedStateHandle.set(MUSIC_LAST_SEARCH_TERM, term)
    }

    fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun getSavedSearch(): String {
        return savedStateHandle.get(MUSIC_LAST_SEARCH_TERM) ?: SearchParams.INIT_SEARCH_TERM
    }

    private fun search(term: String) {
        filterTextAll.postValue(term)
    }

    /**
     * Events Called by databinding
     */

    fun refresh() {
        search(getSavedSearch())
    }


    private var _errorLoading: MutableLiveData<DataSourceException?> =
        MutableLiveData()
    val errorLoading: LiveData<DataSourceException?> = _errorLoading

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


    private val _openMusicEvent = MutableLiveData<Event<Long>>()
    val openMusicEvent: LiveData<Event<Long>> = _openMusicEvent

    val empty: LiveData<Boolean> = Transformations.map(musicList!!) {
        it.isEmpty()
    }

}

// Used to save the current filtering in SavedStateHandle.
const val MUSIC_LAST_SEARCH_TERM = "MUSIC_LAST_SEARCH_TERM"
