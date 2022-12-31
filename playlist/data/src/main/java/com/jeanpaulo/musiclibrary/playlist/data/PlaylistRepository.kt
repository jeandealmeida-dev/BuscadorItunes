package com.jeanpaulo.musiclibrary.playlist.data

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.database.dao.PlaylistDao
import com.jeanpaulo.musiclibrary.core.repository.database.dao.JoinPlaylistMusicDao
import com.jeanpaulo.musiclibrary.core.repository.database.entity.JoinPlaylistMusicEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.core.repository.database.mapper.toModel
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface PlaylistRepository {
    fun getPlaylist(playlistId: Long): Single<Playlist>

    fun saveMusicInPlaylist(music: Music, playlistId: Long): Completable

    fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Completable

    fun deletePlaylist(playlistId: Long): Completable

    fun getPlaylists(): Flowable<List<Playlist>>
}

class PlaylistRepositoryImpl @Inject constructor(
    private val playlistDao: PlaylistDao,
    private val joinPlaylistMusicDao: JoinPlaylistMusicDao,
) : PlaylistRepository {

    override fun getPlaylist(playlistId: Long): Single<Playlist> =
        playlistDao.getPlaylistById(playlistId).map { it?.toModel() }

    override fun saveMusicInPlaylist(music: Music, playlistId: Long): Completable =
        joinPlaylistMusicDao.insert(JoinPlaylistMusicEntity(playlistId = playlistId, musicId = music.id));


    override fun removeMusicFromPlaylist(
        musicId: Long,
        playlistId: Long
    ): Completable = joinPlaylistMusicDao.removeMusicFromPlaylist(musicId, playlistId)

    override fun deletePlaylist(playlistId: Long) : Completable =
        playlistDao.deletePlaylistById(playlistId = playlistId)

    override fun getPlaylists(): Flowable<List<Playlist>> =
        playlistDao.getPlaylists().map { list -> list.map { it.toModel() } }
}