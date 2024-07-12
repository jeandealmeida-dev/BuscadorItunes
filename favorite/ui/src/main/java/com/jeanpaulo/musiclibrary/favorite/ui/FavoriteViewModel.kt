package com.jeanpaulo.musiclibrary.favorite.ui

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.music_player.model.MPPlaylist
import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    object Loading : FavoriteState()
    data class PlaySong(val music: MPSong) : FavoriteState()
    data class PlaySongList(val playlist: MPPlaylist) : FavoriteState()
    data class ShowMusicOptions(val music: MPSong) : FavoriteState()
    data class Removed(val music: MPSong) : FavoriteState()
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

    fun playMusic(music: MPSong) {
        _favoriteState.value = FavoriteState.PlaySong(music)
    }

    fun playSongList(songs: List<SongUIModel>) {
        val playlist = MPPlaylist(songs = songs.map { it.convertToSong() }.toMutableList())
        _favoriteState.value = FavoriteState.PlaySongList(playlist)
    }

    fun options(music: MPSong) {
        _favoriteState.value = FavoriteState.ShowMusicOptions(music)
    }

    fun remove(music: MPSong) {
        compositeDisposable.add(
            interactor.removeFromFavorites(music.id)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    _favoriteState.postValue(FavoriteState.Removed(music))
                }, {
                    it.printStackTrace()
                })
        )
    }
}