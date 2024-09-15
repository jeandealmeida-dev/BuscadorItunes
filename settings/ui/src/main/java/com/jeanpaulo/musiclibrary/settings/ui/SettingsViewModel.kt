package com.jeanpaulo.musiclibrary.settings.ui

import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {

    fun clearDatabase() {
        compositeDisposable.add(
            playlistInteractor.deleteAllPlaylists()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe()
        )
    }
}