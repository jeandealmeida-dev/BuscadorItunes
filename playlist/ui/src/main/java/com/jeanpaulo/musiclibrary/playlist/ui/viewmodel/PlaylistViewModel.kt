package com.jeanpaulo.musiclibrary.playlist.view

import androidx.lifecycle.*
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class PlaylistState {
    object Loading : PlaylistState()
    object Error : PlaylistState()
    object Success : PlaylistState()
}

class PlaylistViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: PlaylistInteractor,
) : BaseViewModel() {

    private val _playlistState = MutableLiveData<PlaylistState>()
    val playlistState: LiveData<PlaylistState> get() = _playlistState

    private val _playlistList = MutableLiveData<List<Playlist>>()
    val playlistList: LiveData<List<Playlist>> get() = _playlistList

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
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _playlistState.value = PlaylistState.Loading
                }
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe({
                    _playlistState.value = PlaylistState.Success
                    refresh()
                }, {
                    _playlistState.value = PlaylistState.Error
                })
        )
    }

    fun getPlaylistList() {
        compositeDisposable.add(
            interactor.getPlaylist()
                .subscribeOn(mainScheduler)
                .observeOn(ioScheduler)
                .doOnSubscribe {
                    _playlistState.postValue(PlaylistState.Loading)
                }
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe({ playlistList ->
                    _playlistList.postValue(playlistList)
                    _playlistState.postValue(PlaylistState.Success)
                }, {
                    _playlistState.postValue(PlaylistState.Error)
                })
        )
    }
}