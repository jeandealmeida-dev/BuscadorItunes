package com.jeanpaulo.musiclibrary.search.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.player.mp.MPService
import com.jeanpaulo.musiclibrary.search.domain.SearchInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

class SearchViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val searchInteractor: SearchInteractor,
    private val favoriteInteractor: FavoriteInteractor,
) : BaseViewModel() {

    private val _searchingState = MutableLiveData<ViewState<PagingData<SongUIModel>>>()
    val searchingState: LiveData<ViewState<PagingData<SongUIModel>>> get() = _searchingState

    fun init() {
        setCurrentQuery(DEFAULT_QUERY)
    }

    fun setCurrentQuery(query: String) {
        compositeDisposable.add(
            searchInteractor.getSearchResults(query)
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _searchingState.postValue(ViewState.Loading)
                }
                .observeOn(mainScheduler)
                .map { paged ->
                    paged.map { SongUIModel.fromModel(it) }
                }
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ result ->
                    _searchingState.postValue(ViewState.Success(result))
                }, {
                    it.printStackTrace()
                    _searchingState.postValue(ViewState.Error)
                })
        )
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

    fun playMusic(context: Context, song: SongUIModel) {
        val mpSong = song.convertToSong().toMPSong()
        MPService.playSongList(context, listOf(mpSong))
    }

    companion object {
        private const val DEFAULT_QUERY = "Pink Floyd"
    }
}
