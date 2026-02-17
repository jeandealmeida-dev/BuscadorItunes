package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.ds.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.domain.FavoriteInteractor
import com.jeanpaulo.musiclibrary.player.mp.MPService
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

sealed class FavoriteAction {
    data class ShowMusicOptions(val music: SongUIModel) : FavoriteAction()
    data class Removed(val music: SongUIModel) : FavoriteAction()
}

class FavoriteViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val interactor: FavoriteInteractor
) : BaseViewModel() {

    private val _favoriteState = MutableLiveData<ViewState<List<SongUIModel>>>()
    val favoriteState: LiveData<ViewState<List<SongUIModel>>> get() = _favoriteState

    private var _favoriteAction = MutableLiveData<FavoriteAction>()
    val favoriteAction: LiveData<FavoriteAction> get() = _favoriteAction

    fun getFavoriteList() {
        compositeDisposable.add(
            interactor.getFavoriteMusics()
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _favoriteState.postValue(ViewState.Loading)
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ favorites ->
                    if (favorites.isEmpty()) {
                        _favoriteState.postValue(ViewState.Empty)
                        return@subscribe
                    }

                    val musicFiltered = favorites
                        .mapNotNull { it.music }
                        .map { SongUIModel.fromModel(it) }

                    _favoriteState.postValue(ViewState.Success(musicFiltered))
                }, {
                    _favoriteState.postValue(ViewState.Error)
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
        _favoriteAction.value = FavoriteAction.ShowMusicOptions(song)
    }

    fun remove(song: SongUIModel) {
        compositeDisposable.add(
            interactor.removeFromFavorites(song.musicId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe({
                    _favoriteAction.value = FavoriteAction.Removed(song)
                }, {
                    it.printStackTrace()
                })
        )
    }
}