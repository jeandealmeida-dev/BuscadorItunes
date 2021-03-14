package com.jeanpaulo.buscador_itunes.view_model

import androidx.lifecycle.*
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource
import com.jeanpaulo.buscador_itunes.util.params.SearchParams
import com.jeanpaulo.buscador_itunes.repository.remote.paged_seach.PagedSearchDataSourceFactory
import io.reactivex.disposables.CompositeDisposable
import com.jeanpaulo.buscador_itunes.util.Event
import com.jeanpaulo.buscador_itunes.model.util.Result as Result1
import kotlinx.coroutines.launch


class SearchViewModel(
    private val musicRepository: MusicRemoteDataSource,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _forceUpdate = MutableLiveData<Boolean>(false)

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _openMusicEvent = MutableLiveData<Event<Long>>()
    val openMusicEvent: LiveData<Event<Long>> = _openMusicEvent

    // Not used at the moment
    private val isDataLoadingError = MutableLiveData<Boolean>()

    private var _musicList: LiveData<PagedList<Music>>? = null

    val musicList: LiveData<PagedList<Music>>? = _musicList

    // This LiveData depends on another so we can use a transformation.
    val empty: LiveData<Boolean> = Transformations.map(musicList!!) {
        it.isEmpty()
    }


    var filterTextAll = MutableLiveData<String>(SearchParams.INIT_SEARCH_TERM)


    init {

        val config = PagedList.Config.Builder()
            .setPageSize(SearchParams.SEARCH_PAGE_SIZE)
            .setInitialLoadSizeHint(SearchParams.SEARCH_PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()

        val dataSourceFactory =
            PagedSearchDataSourceFactory(
                repository = musicRepository,
                executor = CompositeDisposable(),
                searchKey = filterTextAll.toString()
            )

        _musicList =
            Transformations.switchMap<String, PagedList<Music>>(
                filterTextAll
            ) { input ->

                dataSourceFactory.setSearchKey(input)
                LivePagedListBuilder(dataSourceFactory, config).build()
            }

        setFiltering(getSavedSearch())
    }

    fun setFiltering(term: String) {
        savedStateHandle.set(MUSIC_LAST_SEARCH_TERM, term)

        // Refresh list
        loadMusic(true)
    }

    fun loadMusic(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    fun refresh() {
        loadMusic(true)
    }

    private fun showSnackbarMessage(message: Int) {
        _snackbarText.value = Event(message)
    }

    private fun getSavedSearch(): String {
        return savedStateHandle.get(MUSIC_LAST_SEARCH_TERM) ?: String()
    }
}

// Used to save the current filtering in SavedStateHandle.
const val MUSIC_LAST_SEARCH_TERM = "MUSIC_LAST_SEARCH_TERM"
