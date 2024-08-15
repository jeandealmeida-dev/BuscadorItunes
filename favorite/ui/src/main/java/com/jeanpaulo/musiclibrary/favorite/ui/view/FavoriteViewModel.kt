package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.content.Context
import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.music_player.MPService
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

sealed class FavoriteState {
    data object Loading : FavoriteState()
    data class ShowMusicOptions(val music: SongUIModel) : FavoriteState()
    data class Removed(val music: SongUIModel) : FavoriteState()
    data class Loaded(val musicList: List<SongUIModel>) : FavoriteState()
    data object Empty : FavoriteState()
    data object Error : FavoriteState()
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
                //.delay(500, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    if(favorites.isEmpty()){
                        _favoriteState.postValue(FavoriteState.Empty)
                        return@subscribe
                    }

                    val musicFiltered = favorites.map {
                        it.music?.let { music ->
                            SongUIModel.fromModel(music)
                        } ?: SongUIModel.fromModel(Music(musicId = it.musicId, trackName = ""))
                    }
                    _favoriteState.postValue(FavoriteState.Loaded(musicFiltered))
                }, {
                    _favoriteState.postValue(FavoriteState.Error)
                    it.printStackTrace()
                })
        )
    }

    fun playMusic(context: Context, song: SongUIModel) {
        MPService.playSong(context, song.convertToSong())
    }

    fun playSongList(context: Context, songs: List<SongUIModel>) {
        MPService.playSongList(
            context,
            songs.map { it.convertToSong() }
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