package com.jeanpaulo.musiclibrary.favorite.ui

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    data object Loading : FavoriteState()
    data class PlaySong(val music: SongUIModel) : FavoriteState()
    data class PlaySongList(val playlist: List<SongUIModel>) : FavoriteState()
    data class ShowMusicOptions(val music: SongUIModel) : FavoriteState()
    data class Removed(val music: SongUIModel) : FavoriteState()
    data class Loaded(val musicList: List<SongUIModel>) : FavoriteState()
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
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _favoriteState.postValue(FavoriteState.Loading)
                }
                .delay(500, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    val musicFiltered = favorites.map { SongUIModel.fromModel(it.music) }
                    _favoriteState.postValue(FavoriteState.Loaded(musicFiltered))
                }, {
                    it.printStackTrace()
                })
        )
    }

    fun playMusic(song: SongUIModel) {
        _favoriteState.value = FavoriteState.PlaySong(song)
    }

    fun playSongList(songs: List<SongUIModel>) {
        _favoriteState.value = FavoriteState.PlaySongList(songs)
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