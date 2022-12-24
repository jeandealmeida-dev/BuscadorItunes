package com.jeanpaulo.musiclibrary.playlist.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.data.PlaylistRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistInteractor{
    fun getPlaylist(): Flowable<List<Playlist>>
    fun getPlaylist(playlistId: Long): Single<Playlist>
    fun deletePlaylist(playlistId: Long): Completable
}

class PlaylistInteractorImpl @Inject constructor(
    val repository: PlaylistRepository
) : PlaylistInteractor {

    override fun getPlaylist(): Flowable<List<Playlist>> {
        return repository.getPlaylists()
    }

    override fun getPlaylist(playlistId: Long): Single<Playlist> {
        return repository.getPlaylist(playlistId = playlistId)
    }

    override fun deletePlaylist(playlistId: Long): Completable {
        return repository.deletePlaylist(playlistId = playlistId)
    }
}