package com.jeanpaulo.musiclibrary.settings.ui

import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistInteractor
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject
import javax.inject.Named

class SettingsViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val playlistInteractor: PlaylistInteractor,
) : BaseViewModel() {
    fun clearDatabase() {
        playlistInteractor.deleteAllPlaylists()
    }
}