package com.jeanpaulo.musiclibrary.search.ui.viewmodel

import android.view.View
import androidx.lifecycle.*
import androidx.paging.cachedIn
import androidx.paging.map
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class SearchState {
    object Loading : SearchState()
    object Error : SearchState()
    object Success : SearchState()
    class OpenDetail(val view: View, val music: SimpleMusicDetailUIModel) : SearchState()
    class PlaySong(val music: MPSong) : SearchState()
    class Options(val music: MPSong) : SearchState()
}

class SearchViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val searchInteractor: SearchInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) : BaseViewModel() {

    private val _searchingState = MutableLiveData<SearchState>()
    val searchingState: LiveData<SearchState> get() = _searchingState

    private var currentQuery = MutableLiveData(DEFAULT_QUERY)

    val musicList = currentQuery.switchMap { query ->
        return@switchMap searchInteractor
            .getSearchResults(query)
            .asLiveData()
            .map { pagged ->
                pagged.map { SongUIModel.fromModel(it) }
            }
            .cachedIn(viewModelScope)
    }

    fun setCurrentQuery(it: String) {
        currentQuery.value = it
    }

    fun playMusic(music: MPSong) {
        _searchingState.value = SearchState.PlaySong(music)
    }

    fun options(music: MPSong) {
        _searchingState.value = SearchState.Options(music)
    }

    fun addInPlaylist(music: MPSong) {
        TODO("Not yet implemented")
    }

    fun addInFavorite(music: MPSong) {
        searchInteractor.getSearchMusic(musicId = music.id)?.let {
            compositeDisposable.add(
                favoriteInteractor.saveInFavorite(it)
                    .subscribeOn(ioScheduler)
                    .observeOn(mainScheduler)
                    .subscribe()
            )
        }
    }


    companion object {
        private const val DEFAULT_QUERY = "Nirvana"
    }
}
