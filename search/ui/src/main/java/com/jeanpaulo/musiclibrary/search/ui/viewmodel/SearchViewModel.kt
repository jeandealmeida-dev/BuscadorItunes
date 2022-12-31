package com.jeanpaulo.musiclibrary.search.ui.viewmodel

import android.view.View
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import com.jeanpaulo.musiclibrary.search.ui.model.mapper.toUIModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class SearchState {
    object Loading : SearchState()
    object Error : SearchState()
    object Success : SearchState()
    class OpenDetail(val view: View, val music: SimpleMusicDetailUIModel) : SearchState()
}

class SearchViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val searchInteractor: SearchInteractor,
) : BaseViewModel() {

    private val _state = MutableLiveData<SearchState>()
    val state: LiveData<SearchState> get() = _state

    val snackbarText = MutableLiveData(DEFAULT_QUERY)

    private var currentQuery = MutableLiveData(DEFAULT_QUERY)
    val musicList = currentQuery.switchMap { query ->
        return@switchMap searchInteractor
            .getSearchResults(query)
            .map { pagged -> pagged.map { it.toUIModel() } }
            .cachedIn(viewModelScope)
    }

    override fun onCreate() {
        super.onCreate()
        currentQuery.value = "Nirvana"
    }

    fun refresh(){
        currentQuery.value
    }

    fun setCurrentQuery(it: String) {
        currentQuery.value = it
    }

    fun openMusicDetail(view: View, music: SimpleMusicDetailUIModel) {
        _state.value = SearchState.OpenDetail(view, music)
    }


    companion object {
        private const val DEFAULT_QUERY = "top10"
    }
}

// Used to save the current filtering in SavedStateHandle.
const val MUSIC_LAST_SEARCH_TERM = "MUSIC_LAST_SEARCH_TERM"
