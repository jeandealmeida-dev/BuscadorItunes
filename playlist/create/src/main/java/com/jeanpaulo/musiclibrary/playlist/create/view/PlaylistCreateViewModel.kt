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

package com.jeanpaulo.musiclibrary.playlist.create.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.playlist.create.domain.PlaylistCreateInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Named

sealed class PlaylistCreateState {
    object Loading : PlaylistCreateState()
    object Error : PlaylistCreateState()
    object Success : PlaylistCreateState()
}
class PlaylistCreateViewModel(
    @Named("MainScheduler") private val mainScheduler: Scheduler,
    @Named("IOScheduler") private val ioScheduler: Scheduler,
    private val playlistInteractor: PlaylistCreateInteractor,
) : BaseViewModel() {

    private val _playlistCreateState = MutableLiveData<PlaylistCreateState>()
    val playlistCreateState: LiveData<PlaylistCreateState> get() = _playlistCreateState

    private fun createPlaylist(playlist: Playlist) {
        compositeDisposable.add(
            playlistInteractor.savePlaylist(playlist)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    _playlistCreateState.value = PlaylistCreateState.Loading
                }
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribe({ playlistId ->
                    _playlistCreateState.value = PlaylistCreateState.Success
                }, {
                    _playlistCreateState.value = PlaylistCreateState.Error
                })
        )
    }
}