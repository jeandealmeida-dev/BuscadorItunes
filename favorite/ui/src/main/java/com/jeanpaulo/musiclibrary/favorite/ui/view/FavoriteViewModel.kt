package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.player.mp.MPService
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    data class Wrapper(val viewState: ViewState<List<SongUIModel>>) : FavoriteState()

    data class ShowMusicOptions(val music: SongUIModel) : FavoriteState()
    data class Removed(val music: SongUIModel) : FavoriteState()
}

class FavoriteViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

    private val _favoriteState = MutableLiveData<FavoriteState>()
    val favoriteState: LiveData<FavoriteState> get() = _favoriteState

    override fun onCreate() {
        super.onCreate()
        getFavoriteList()
    }

    fun refresh() {
        getFavoriteList()
    }

    fun getFavoriteList() {
        compositeDisposable.add(
            interactor.getFavoriteMusics()
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    val loading = FavoriteState.Wrapper(ViewState.Loading)
                    _favoriteState.postValue(loading)
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    if (favorites.isEmpty()) {
                        val emptyState = FavoriteState.Wrapper(ViewState.Empty)
                        _favoriteState.postValue(emptyState)
                        return@subscribe
                    }

                    val musicFiltered = favorites.map {
                        it.music?.let { music ->
                            SongUIModel.fromModel(music)
                        } ?: SongUIModel.fromModel(Music(musicId = it.musicId, trackName = ""))
                    }
                    val successState = FavoriteState.Wrapper(ViewState.Success(musicFiltered))
                    _favoriteState.postValue(successState)
                }, {
                    val errorState = FavoriteState.Wrapper(ViewState.Error)
                    _favoriteState.postValue(errorState)

                    it.printStackTrace()
                })
        )
    }

    fun playMusic(context: Context, song: SongUIModel) {
        val mpSong = song.convertToSong().toMPSong()
        MPService.playSongList(context, listOf(mpSong))
    }

    fun playSongList(context: Context, songs: List<SongUIModel>) {
        MPService.playSongList(
            context,
            songs.map { it.convertToSong().toMPSong() }
        )
    }

    fun options(song: SongUIModel) {
        _favoriteState.value = FavoriteState.ShowMusicOptions(song)
    }

    fun remove(song: SongUIModel) {
        compositeDisposable.add(
            interactor.removeFromFavorites(song.musicId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    _favoriteState.postValue(FavoriteState.Removed(song))
                }, {
                    it.printStackTrace()
                })
        )
    }
}