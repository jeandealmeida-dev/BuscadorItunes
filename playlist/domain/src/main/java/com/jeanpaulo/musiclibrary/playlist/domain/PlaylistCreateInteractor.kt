package com.jeanpaulo.musiclibrary.playlist.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.data.PlaylistCreateRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistCreateInteractor {
    fun savePlaylist(playlistId: Playlist): Single<Long>
}

class PlaylistCreateInteractorImpl @Inject constructor(
    val repository: PlaylistCreateRepository
) : PlaylistCreateInteractor {

    override fun savePlaylist(playlist: Playlist): Single<Long> {
        return repository.savePlaylist(playlist)
    }
}