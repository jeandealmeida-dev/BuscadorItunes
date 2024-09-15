package com.jeanpaulo.musiclibrary.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.player.mp.MPService
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import com.jeanpaulo.musiclibrary.search.ui.SearchUIModel
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class SearchState {
    object Loading : SearchState()
    object Error : SearchState()
    data class Success(val musicList: PagingData<SearchUIModel>) : SearchState()
    class Options(val music: SearchUIModel) : SearchState()
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
                    paged.map { SearchUIModel.fromModel(it) }
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

    fun playMusic(context: Context, song: SearchUIModel) {
        val mpSong = song.convertToSong().toMPSong()
        MPService.playSongList(context, listOf(mpSong))
    }

    fun options(song: SearchUIModel) {
        _searchingState.value = SearchState.Options(song)
    }

    fun addInFavorite(musicId: Long) {
        searchInteractor.getSearchMusic(musicId = musicId)?.let {
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
