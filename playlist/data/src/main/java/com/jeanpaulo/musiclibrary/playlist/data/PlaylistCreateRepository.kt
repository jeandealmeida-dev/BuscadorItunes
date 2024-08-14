package com.jeanpaulo.musiclibrary.playlist.data

import com.jeanpaulo.musiclibrary.core.repository.database.dao.PlaylistDao
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.core.repository.database.entity.PlaylistEntity
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistCreateRepository {

    fun savePlaylist(playlist: Playlist): Single<Long>
}

class PlaylistCreateRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao
) : PlaylistCreateRepository {

    override fun savePlaylist(playlist: Playlist): Single<Long> =
        playlistDao.insertPlaylist(playlist = PlaylistEntity.from(playlist))


}