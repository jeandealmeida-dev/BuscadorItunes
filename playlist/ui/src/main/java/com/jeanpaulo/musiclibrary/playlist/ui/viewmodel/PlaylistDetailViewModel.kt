/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanpaulo.musiclibrary.playlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistDetailInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class PlaylistDetailState {
    object Loading : PlaylistDetailState()
    object Error : PlaylistDetailState()
    data class Success(val playlist: Playlist) : PlaylistDetailState()
}
class PlaylistDetailViewModel @Inject constructor(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val interactor: PlaylistDetailInteractor,
) : BaseViewModel() {

    private val _playlistDetailState = MutableLiveData<PlaylistDetailState>()
    val playlistDetailState: LiveData<PlaylistDetailState> get() = _playlistDetailState

    fun getPlaylist(playlistId: Long) {
        compositeDisposable.add(
            interactor.getPlaylist(playlistId)
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _playlistDetailState.value = PlaylistDetailState.Loading
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ playlist ->
                    _playlistDetailState.value = PlaylistDetailState.Success(playlist)
                }, {
                    _playlistDetailState.value = PlaylistDetailState.Error
                })
        )
    }
}
