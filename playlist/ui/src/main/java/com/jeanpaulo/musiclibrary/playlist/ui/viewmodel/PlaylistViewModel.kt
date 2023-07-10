package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class PlaylistListState {
    object Loading : PlaylistListState()
    object Error : PlaylistListState()
    data class Success(val playlistList: List<Playlist>) : PlaylistListState()
}

sealed class PlaylistDeleteState {
    object Loading : PlaylistDeleteState()
    object Error : PlaylistDeleteState()
    object Success : PlaylistDeleteState()
}

class PlaylistViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: PlaylistInteractor,
) : BaseViewModel() {

    private val _playlistListState = MutableLiveData<PlaylistListState>()
    val playlistListState: LiveData<PlaylistListState> get() = _playlistListState

    private val _playlistDeleteState = MutableLiveData<PlaylistDeleteState>()
    val playlistDeleteState: LiveData<PlaylistDeleteState> get() = _playlistDeleteState

    override fun onCreate() {
        super.onCreate()
        getPlaylistList()
    }

    fun refresh() {
        getPlaylistList()
    }

    fun deletePlaylist(playlistId: Long) {
        compositeDisposable.add(
            interactor.deletePlaylist(playlistId)
                .subscribeOn(ioScheduler)
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _playlistDeleteState.value = PlaylistDeleteState.Loading
                }
                .subscribe({
                    _playlistDeleteState.value = PlaylistDeleteState.Success
                    refresh()
                }, {
                    _playlistDeleteState.value = PlaylistDeleteState.Error
                })
        )
    }

    fun getPlaylistList() {
        compositeDisposable.add(
            interactor.getPlaylist()
                .subscribeOn(mainScheduler)
                .delay(500, TimeUnit.MILLISECONDS)
                .observeOn(ioScheduler)
                .doOnSubscribe {
                    _playlistListState.postValue(PlaylistListState.Loading)
                }
                .subscribe({ playlistList ->
                    _playlistListState.postValue(PlaylistListState.Success(playlistList))
                }, {
                    _playlistListState.postValue(PlaylistListState.Error)
                })
        )
    }
}