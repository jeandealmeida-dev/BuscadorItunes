package com.jeanpaulo.musiclibrary.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.music_player.MPService
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class SearchState {
    object Loading : SearchState()
    object Error : SearchState()
    data class Success(val musicList: PagingData<SongUIModel>) : SearchState()
    class Options(val music: SongUIModel) : SearchState()
}

class SearchViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val searchInteractor: SearchInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) : BaseViewModel() {

    private val _searchingState = MutableLiveData<SearchState>()
    val searchingState: LiveData<SearchState> get() = _searchingState

    fun init() {
        setCurrentQuery(DEFAULT_QUERY)
    }

    fun setCurrentQuery(query: String) {
        compositeDisposable.add(
            searchInteractor.getSearchResults(query)
                .subscribeOn(ioScheduler)
                //.delay(500, TimeUnit.MILLISECONDS)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _searchingState.value = SearchState.Loading
                }
                .map { paged ->
                    paged.map { SongUIModel.fromModel(it) }
                }
                .subscribe(
                    { result ->
                        _searchingState.postValue(SearchState.Success(result))
                    },
                    { error ->
                        _searchingState.postValue(SearchState.Error)
                    }
                )
        )
    }

    fun playMusic(context: Context, song: SongUIModel) {
        MPService.playSong(context, song.convertToSong())
    }

    fun options(song: SongUIModel) {
        _searchingState.value = SearchState.Options(song)
    }

    fun addInFavorite(song: SongUIModel) {
        searchInteractor.getSearchMusic(musicId = song.musicId)?.let {
            compositeDisposable.add(
                favoriteInteractor.saveInFavorite(it)
                    .subscribeOn(ioScheduler)
                    .observeOn(mainScheduler)
                    .subscribe({}, { exception ->
                        exception.printStackTrace()
                    })
            )
        }
    }

    companion object {
        private const val DEFAULT_QUERY = "Pink Floyd"
    }
}
