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
package com.jeanpaulo.buscador_itunes.util

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.buscador_itunes.view.favorite.FavoriteViewModel
import com.jeanpaulo.buscador_itunes.view.fragment.add_edit_playlist.AddEditPlaylistViewModel
import com.jeanpaulo.buscador_itunes.view.fragment.playlist_list.PlaylistViewModel
import com.jeanpaulo.buscador_itunes.view.music.music_search.music_detail.MusicDetailViewModel
import com.jeanpaulo.buscador_itunes.view.music.music_search.SearchViewModel
import com.jeanpaulo.buscador_itunes.view.playlist.detail.DetailPlaylistViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val dataSource: IDataSource,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(
                    dataSource,
                    handle
                )
            isAssignableFrom(MusicDetailViewModel::class.java) ->
                MusicDetailViewModel(
                    dataSource,
                    handle
                )
            isAssignableFrom(PlaylistViewModel::class.java) ->
                PlaylistViewModel(
                    dataSource,
                    handle
                )
            isAssignableFrom(AddEditPlaylistViewModel::class.java) ->
                AddEditPlaylistViewModel(
                    dataSource
                )
            isAssignableFrom(DetailPlaylistViewModel::class.java) ->
                DetailPlaylistViewModel(
                    dataSource
                )
            isAssignableFrom(FavoriteViewModel::class.java) ->
                FavoriteViewModel(
                    dataSource,
                    handle
                )

            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}
