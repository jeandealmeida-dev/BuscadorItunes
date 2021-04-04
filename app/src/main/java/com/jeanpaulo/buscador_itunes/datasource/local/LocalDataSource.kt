package com.jeanpaulo.buscador_itunes.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jeanpaulo.buscador_itunes.datasource.local.dao.MusicDao
import com.jeanpaulo.buscador_itunes.datasource.local.dao.PlaylistDao
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.jeanpaulo.buscador_itunes.model.util.Result


/**
 * Concrete implementation of a data source as a db.
 */
class LocalDataSource internal constructor(
    private val musicDao: MusicDao,
    private val playlistDao: PlaylistDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ILocalDataSource {

    override fun observeMusics(): LiveData<Result<List<Music>>> {
        return musicDao.observeMusics().map {
            Result.Success(it.map { it.toModel() })
        }
    }

    override suspend fun getMusics(): Result<List<Music>> {
        return try {
            Result.Success(musicDao.getMusics().map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }

    override fun observeMusic(taskId: Long): LiveData<Result<Music>> {
        return musicDao.observeMusicById(taskId).map {
            Result.Success(it.toModel())
        }
    }

    override suspend fun refreshMusic(taskId: Long) {
        // NO-OP
    }

    override suspend fun refreshMusics() {
        // NO-OP
    }

    override suspend fun getMusic(musicId: Long): Result<Music> = withContext(ioDispatcher) {
        try {
            val musicEntity = musicDao.getMusicById(musicId)
            if (musicEntity != null) {
                return@withContext Result.Success(musicEntity.toModel())
            } else {
                return@withContext Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NULL_EXCEPTION,
                        "Music not found!"
                    )
                )
            }
        } catch (e: Exception) {
            return@withContext Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }

    override suspend fun saveMusic(music: Music) = withContext(ioDispatcher) {
        musicDao.insertMusic(music.toEntity())
    }

    override suspend fun completeMusic(task: Music) = withContext(ioDispatcher) {
        //musicDao.updateCompleted(task.id, true)
    }

    override suspend fun completeMusic(taskId: Long) {
        //musicDao.updateCompleted(taskId, true)
    }

    override suspend fun activateMusic(task: Music) = withContext(ioDispatcher) {
        //musicDao.updateCompleted(task.id, false)
    }

    override suspend fun activateMusic(taskId: Long) {
        //musicDao.updateCompleted(taskId, false)
    }

    override suspend fun clearCompletedMusics() = withContext<Unit>(ioDispatcher) {
        //musicDao.deleteCompletedMusics()
    }

    override suspend fun deleteAllMusics() = withContext(ioDispatcher) {
        musicDao.deleteMusics()
    }

    override suspend fun deleteMusic(taskId: Long) = withContext<Unit>(ioDispatcher) {
        musicDao.deleteMusicById(taskId)
    }

    override suspend fun getPlaylists(): Result<List<Playlist>> =
        withContext<Result<List<Playlist>>>(ioDispatcher) {
            try {
                Result.Success(playlistDao.getPlaylists().map { it.toModel() })
            } catch (e: Exception) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.UNKNOWN_EXCEPTION,
                        e.toString()
                    )
                )
            }
        }

    override suspend fun getPlaylist(playlistId: String): Result<Playlist> {
        return withContext<Result<Playlist>>(ioDispatcher) {
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
        }
    }

    override suspend fun savePlaylist(playlist: Playlist): Result<Boolean> {
        return withContext<Result<Boolean>>(ioDispatcher) {
            try {
                playlistDao.insertPlaylist(playlist.toEntity())
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
    }

    override suspend fun deletePlaylist(playlistId: String): Result<Boolean> {
        return withContext<Result<Boolean>>(ioDispatcher) {
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
    }
}
