package com.jeanpaulo.musiclibrary.playlist.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.data.PlaylistDetailRepository
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistDetailInteractor{
    fun getPlaylist(playlistId: Long): Single<Playlist>
    fun deletePlaylist(playlistId: Long): Completable
    fun savePlaylist(playlistId: Playlist): Single<Long>
}

class PlaylistDetailInteractorImpl @Inject constructor(
    val playlistRepository: PlaylistDetailRepository
) : PlaylistDetailInteractor {

    override fun getPlaylist(playlistId: Long): Single<Playlist> {
        return playlistRepository.getPlaylist(playlistId = playlistId)
    }

    override fun deletePlaylist(playlistId: Long): Completable {
        return playlistRepository.deletePlaylist(playlistId = playlistId)
    }

    override fun savePlaylist(playlist: Playlist): Single<Long> {
        return playlistRepository.savePlaylist(playlist)
    }
}