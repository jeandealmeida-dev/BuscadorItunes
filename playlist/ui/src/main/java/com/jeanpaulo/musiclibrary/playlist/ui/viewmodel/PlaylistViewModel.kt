package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class PlaylistListState {
    data object Loading : PlaylistListState()
    data object Error : PlaylistListState()
    data object Empty : PlaylistListState()
    data class Success(val playlistList: List<Playlist>) : PlaylistListState()
}

sealed class PlaylistDeleteState {
    data object Loading : PlaylistDeleteState()
    data object Error : PlaylistDeleteState()
    data object Success : PlaylistDeleteState()
}

class PlaylistViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val interactor: PlaylistInteractor,
) : BaseViewModel() {

    private val _playlistListState = MutableLiveData<PlaylistListState>()
    val playlistListState: LiveData<PlaylistListState> get() = _playlistListState

    private val _playlistDeleteState = MutableLiveData<PlaylistDeleteState>()
    val playlistDeleteState: LiveData<PlaylistDeleteState> get() = _playlistDeleteState

    fun refresh() {
        getPlaylistList()
    }

    fun deletePlaylist(playlistId: Long) {
        compositeDisposable.add(
            interactor.deletePlaylist(playlistId)
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _playlistDeleteState.value = PlaylistDeleteState.Loading
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({
                    _playlistDeleteState.value = PlaylistDeleteState.Success
                }, {
                    _playlistDeleteState.value = PlaylistDeleteState.Error
                })
        )
    }

    fun getPlaylistList() {
        compositeDisposable.add(
            interactor.getPlaylist()
                .subscribeOn(mainScheduler)
                .doOnSubscribe {
                    _playlistListState.postValue(PlaylistListState.Loading)
                }
                .observeOn(ioScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ playlistList ->
                    _playlistListState.postValue(
                        PlaylistListState.Success(playlistList)
                    )
                }, {
                    _playlistListState.postValue(PlaylistListState.Error)
                })
        )
    }
}