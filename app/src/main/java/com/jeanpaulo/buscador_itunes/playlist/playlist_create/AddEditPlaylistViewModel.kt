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

package com.jeanpaulo.buscador_itunes.playlist.playlist_create

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.IDataSource
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Playlist
import com.jeanpaulo.buscador_itunes.util.Event
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.Result
import kotlinx.coroutines.launch

/**
 * ViewModel for the Add/Edit screen.
 */
class AddEditPlaylistViewModel(
    private val dataSource: IDataSource
) : ViewModel() {

    // Two-way databinding, exposing MutableLiveData
    val currentTitle = MutableLiveData<String>()

    // Two-way databinding, exposing MutableLiveData
    val currentDescription = MutableLiveData<String?>()

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    private val _taskUpdatedEvent = MutableLiveData<Event<Unit>>()
    val playlistUpdatedEvent: LiveData<Event<Unit>> = _taskUpdatedEvent

    private var playlistId: Long? = null

    private var isNewPlaylist: Boolean = false

    private var isDataLoaded = false

    fun start(playlistId: Long?) {
        if (_dataLoading.value == true) {
            return
        }

        this.playlistId = playlistId
        if (playlistId == null) {
            // No need to populate, it's a new task
            isNewPlaylist = true
            return
        }
        if (isDataLoaded) {
            // No need to populate, already have data.
            return
        }

        isNewPlaylist = false
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
        currentTitle.postValue(playlist.title)
        currentDescription.postValue(playlist.description)

        _dataLoading.value = false
        isDataLoaded = true
    }

    private fun onDataNotAvailable() {
        _dataLoading.value = false
    }

    // Called when clicking on fab.
    fun savePlaylist() {

        val playlist = Playlist(
            title =  currentTitle.value ?: "",
            description = currentDescription.value
        )
        if (currentTitle.value.isNullOrEmpty()) {
            _snackbarText.value = Event(R.string.empty_playlist_message)
            return
        }

        if (playlist.playlistId > 0) {
            updatePlaylist(playlist)
        } else {
            createPlaylist(playlist)
        }
    }

    private fun createPlaylist(playlist: Playlist) = viewModelScope.launch {
        val result: Result<Long> = dataSource.savePlaylist(playlist)
        val playlistId = (result is Result.Success)?.let { (result as Result.Success).data }
        _taskUpdatedEvent.value = Event(Unit)
    }

    private fun updatePlaylist(playlist: Playlist) {
        if (isNewPlaylist) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        viewModelScope.launch {
            dataSource.savePlaylist(playlist)
            _taskUpdatedEvent.value = Event(Unit)
        }
    }
}
