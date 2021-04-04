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

package com.jeanpaulo.buscador_itunes.view.playlist.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.Event
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add/Edit screen.
 */
class DetailPlaylistViewModel(
    private val dataSource: IDataSource
) : ViewModel() {

    private val _playlist = MutableLiveData<Playlist>()
    val playlist: LiveData<Playlist> = _playlist

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private var playlistId: String? = null

    private var isDataLoaded = false

    fun start(playlistId: String?) {
        if (_dataLoading.value == true) {
            return
        }

        this.playlistId = playlistId
        if (playlistId == null) {
            return
        }
        if (isDataLoaded) {
            // No need to populate, already have data.
            return
        }
        _dataLoading.value = true

        viewModelScope.launch {
            dataSource.getPlaylist(playlistId).let { result ->
                if (result is Result.Success) {
                    onPlaylistLoaded(result.data)
                } else {
                    onDataNotAvailable()
                }
            }
        }
    }

    private fun onPlaylistLoaded(playlist: Playlist) {
        _playlist.postValue(playlist)

        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }


    fun editPlaylist() {


    }
}
