package com.jeanpaulo.musiclibrary.playlist.detail.data

import com.jeanpaulo.musiclibrary.core.repository.database.dao.PlaylistDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.JoinPlaylistMusicDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.JoinPlaylistMusicEntity
import com.jeanpaulo.musiclibrary.core.repository.database.mapper.toEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistDetailRepository {
    fun getPlaylist(playlistId: Long): Single<Playlist>

    fun savePlaylist(playlist: Playlist): Single<Long>

    fun saveMusicInPlaylist(music: Music, playlistId: Long): Completable

    fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Completable

    fun deletePlaylist(playlistId: Long): Completable
}

class PlaylistDetailRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val joinPlaylistMusicDao: JoinPlaylistMusicDao,
) : PlaylistDetailRepository {

    override fun getPlaylist(playlistId: Long): Single<Playlist> =
        playlistDao.getPlaylistById(playlistId).map { it?.toModel() }


    override fun savePlaylist(playlist: Playlist): Single<Long> =
        playlistDao.insertPlaylist(playlist.toEntity())


    override fun saveMusicInPlaylist(music: Music, playlistId: Long): Completable =
        joinPlaylistMusicDao.insert(JoinPlaylistMusicEntity(playlistId, music.id));


    override fun removeMusicFromPlaylist(
        musicId: Long,
        playlistId: Long
    ): Completable = joinPlaylistMusicDao.removeMusicFromPlaylist(musicId, playlistId)

    override fun deletePlaylist(playlistId: Long) : Completable =
        playlistDao.deletePlaylistById(playlistId = playlistId)

}