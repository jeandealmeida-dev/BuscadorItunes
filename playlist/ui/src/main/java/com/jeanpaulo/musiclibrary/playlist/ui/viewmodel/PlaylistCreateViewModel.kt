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
import com.jeanpaulo.musiclibrary.commons.base.BaseViewModel
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.IOScheduler
import com.jeanpaulo.musiclibrary.commons.di.qualifiers.MainScheduler
import com.jeanpaulo.musiclibrary.core.BuildConfig
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.domain.PlaylistCreateInteractor
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Named

sealed class PlaylistCreateState {
    object Loading : PlaylistCreateState()
    object Error : PlaylistCreateState()
    data class Success(val playlistId: Long) : PlaylistCreateState()
}

class PlaylistCreateViewModel @Inject constructor(
    @MainScheduler private val mainScheduler: Scheduler,
    @IOScheduler private val ioScheduler: Scheduler,
    private val playlistInteractor: PlaylistCreateInteractor,
) : BaseViewModel() {

    private val _playlistCreateState = MutableLiveData<PlaylistCreateState>()
    val playlistCreateState: LiveData<PlaylistCreateState> get() = _playlistCreateState

    fun createPlaylist(title: String, description: String?) {
        compositeDisposable.add(
            playlistInteractor.savePlaylist(
                Playlist(
                    title = title,
                    description = description
                )
            )
                .subscribeOn(ioScheduler)
                .doOnSubscribe {
                    _playlistCreateState.value = PlaylistCreateState.Loading
                }
                .observeOn(mainScheduler)
                .delay(BuildConfig.DEFAULT_DELAY, TimeUnit.MILLISECONDS)
                .subscribe({ playlistId ->
                    _playlistCreateState.value = PlaylistCreateState.Success(playlistId)
                }, {
                    _playlistCreateState.value = PlaylistCreateState.Error
                })
        )
    }
}
