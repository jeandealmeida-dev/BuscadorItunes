package com.jeanpaulo.buscador_itunes.view_model

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.datasource.MusicDataSource
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import com.jeanpaulo.buscador_itunes.datasource.remote.paged_seach.PagedSearchDataSourceFactory
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

            dataSourceFactory =
                PagedSearchDataSourceFactory(
                    dataSource = dataSource,
                    searchMediaType = SearchParams.SEARCH_MEDIA_TYPE,
                    searchKey = input,
                    networkStateUpdate = {
                        networkState.postValue(it)
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

    var networkState = MutableLiveData<NetworkState>()

    val musicList: LiveData<PagedList<Music>>? = _musicList

    init {
        search(getSavedSearch())
    }

    fun saveLastSearchTerm(term: String) {
        savedStateHandle.set(MUSIC_LAST_SEARCH_TERM, term)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun getSavedSearch(): String {
        return savedStateHandle.get(MUSIC_LAST_SEARCH_TERM) ?: SearchParams.INIT_SEARCH_TERM
    }

    fun search(term: String) {
        filterTextAll.postValue(term)
    }

    /**
     * Events Called by databinding
     */

    fun refresh() {
        search(getSavedSearch())
    }


    val dataLoading: LiveData<Boolean> = Transformations.switchMap<NetworkState, Boolean>(
        networkState
    ) {
        when (it) {
            NetworkState.LOADING -> MutableLiveData<Boolean>(true)
            else -> MutableLiveData<Boolean>(false)
        }
    }


    private val _openMusicEvent = MutableLiveData<Event<Long>>()
    val openMusicEvent: LiveData<Event<Long>> = _openMusicEvent

    val empty: LiveData<Boolean> = Transformations.map(musicList!!) {
        it.isEmpty()
    }

    fun openMusic(musicId: Long?) {
        _openMusicEvent.value = Event(musicId!!)
    }


}

// Used to save the current filtering in SavedStateHandle.
const val MUSIC_LAST_SEARCH_TERM = "MUSIC_LAST_SEARCH_TERM"
