package com.jeanpaulo.buscador_itunes.playlist.mvvm.data

import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.dao.PlaylistDao
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.dao.PlaylistWithMusicDao
import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.Result
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Playlist

interface PlaylistRepository {
    suspend fun getPlaylist(playlistId: Long): Result<Playlist>

    suspend fun savePlaylist(playlist: Playlist): Result<Long>

    suspend fun saveMusicInPlaylist(music: Music, playlistId: Long): Result<Long>

    suspend fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Result<Boolean>

    suspend fun deletePlaylist(playlistId: Long): Result<Boolean>

}

class PlaylistRepositoryImpl internal constructor(
    private val playlistDao: PlaylistDao,
    private val playlistWithMusicDao: PlaylistWithMusicDao,
) : PlaylistRepository {

    override suspend fun getPlaylist(playlistId: Long): Result<Playlist> =
        try {
            val playlist = playlistDao.getPlaylistById(playlistId)
            if (playlist != null)
                Result.Success(playlist.toModel())
            else
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NOT_FOUND_EXCEPTION,
                        "Not found!"
                    )
                )
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )

        }

    override suspend fun savePlaylist(playlist: Playlist): Result<Long> =
        try {
            Result.Success(playlistDao.insertPlaylist(playlist.toEntity()))
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }

    override suspend fun saveMusicInPlaylist(music: Music, playlistId: Long): Result<Long> =
        try {
            val playlistMusicJoin = PlaylistMusicJoin(playlistId, music.id!!)
            val id = playlistWithMusicDao.insert(playlistMusicJoin);
            Result.Success(id)
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }

    override suspend fun removeMusicFromPlaylist(
        musicId: Long,
        playlistId: Long
    ) =
        try {
            playlistWithMusicDao.removeMusicFromPlaylist(musicId, playlistId)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }

    override suspend fun deletePlaylist(playlistId: Long) =
        try {
            playlistDao.deletePlaylistById(playlistId = playlistId)
            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }

}