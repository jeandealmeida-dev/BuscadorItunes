package com.jeanpaulo.buscador_itunes.datasource

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.Music
import kotlinx.coroutines.*
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.datasource.remote.service.ItunesService
import com.jeanpaulo.buscador_itunes.datasource.local.ILocalDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Playlist
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Default implementation of [MusicsRepository]. Single entry point for managing musics' data.
 */
class DefaultMusicRepository(
    private val remote: ItunesService,
    private val local: ILocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IDataSource {

    private var log: org.slf4j.Logger = LoggerFactory.getLogger(DefaultMusicRepository::class.java)

    /*override suspend fun getMusics(forceUpdate: Boolean): Result<List<Music>> {
        // Set app as busy while this function executes.
        wrapEspressoIdlingResource {

            if (forceUpdate) {
                try {
                    updateMusicsFromRemoteDataSource()
                } catch (ex: Exception) {
                    return Result.Error(ex)
                }
            }
            return musicLocalDataSource.getMusics()
        }
    }*/

    override suspend fun searchMusic(
        term: String,
        mediaType: String,
        offset: Int,
        limit: Int
    ): Result<List<Music>> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    remote.searchMusic(term, mediaType, offset, limit)
                        .also {
                            log.info("RESPONSE ->", it)
                        }
                if (response.isSuccessful) {
                    val listJson = response.body()!!.result
                    Result.Success(listJson.map { json -> json.convert() })
                } else
                    Result.Error(
                        DataSourceException(
                            DataSourceException.Error.NULL_EXCEPTION,
                            "Objeto com erro"
                        )
                    )

            } catch (e: SocketTimeoutException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        "NO INTERNET CONNECTION"
                    )
                )
            } catch (e: IOException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            } catch (e: Exception) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.UNKNOWN_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            }
        }

    }

    override suspend fun lookup(term: Long, mediaType: String): Result<Music> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    remote.lookUp(term, mediaType)
                        .also {
                            log.info("RESPONSE ->", it)
                        }
                if (response.isSuccessful) {
                    val musicJson = response.body()!!.result[0]
                    Result.Success(musicJson.convert())
                } else
                    Result.Error(
                        DataSourceException(
                            DataSourceException.Error.NULL_EXCEPTION,
                            "Objeto com erro"
                        )
                    )

            } catch (e: SocketTimeoutException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        "NO INTERNET CONNECTION"
                    )
                )
            } catch (e: IOException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            } catch (e: Exception) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.UNKNOWN_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            }
        }


    }


    override fun observeMusics(): LiveData<Result<List<Music>>> {
        return local.observeMusics()
    }

    override suspend fun getMusics(): Result<List<Music>> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMusics() {
        TODO("Not yet implemented")
    }

    override suspend fun refreshMusic(musicId: Long) {
        updateMusicFromRemoteDataSource(musicId)
    }

    private suspend fun updateMusicsFromRemoteDataSource() {
        /*val remoteMusics = musicRemoteDataSource.getMusics()

        if (remoteMusics is Result.Success) {
            // Real apps might want to do a proper sync, deleting, modifying or adding each music.
            musicLocalDataSource.deleteAllMusics()
            remoteMusics.data.forEach { music ->
                musicLocalDataSource.saveMusic(music)
            }
        } else if (remoteMusics is Result.Error) {
            throw remoteMusics.exception
        }*/
    }

    override fun observeMusic(musicId: Long): LiveData<Result<Music>>? {
        return local.observeMusic(musicId)
    }

    override suspend fun getMusic(musicId: Long): Result<Music>? {
        return null
    }

    private suspend fun updateMusicFromRemoteDataSource(musicId: Long) {
        /*val remoteMusic = musicRemoteDataSource.getMusic(musicId)

        if (remoteMusic is Result.Success) {
            musicLocalDataSource.saveMusic(remoteMusic.data)
        }*/
    }


    override suspend fun saveMusic(music: Music) {
        coroutineScope {
            //launch { musicRemoteDataSource.saveMusic(music) }
            launch { local.saveMusic(music) }
        }
    }

    override suspend fun completeMusic(music: Music) {
        coroutineScope {
            //launch { musicRemoteDataSource.completeMusic(music) }
            launch { local.completeMusic(music) }
        }
    }

    override suspend fun completeMusic(musicId: Long) {
        withContext(ioDispatcher) {
            (getMusicWithId(musicId) as? Result.Success)?.let { it ->
                completeMusic(it.data)
            }
        }
    }

    override suspend fun activateMusic(music: Music) =
        withContext<Unit>(ioDispatcher) {
            coroutineScope {
                //launch { musicRemoteDataSource.activateMusic(music) }
                launch { local.activateMusic(music) }
            }
        }

    override suspend fun activateMusic(musicId: Long) {
        withContext(ioDispatcher) {
            (getMusicWithId(musicId) as? Result.Success)?.let { it ->
                activateMusic(it.data)
            }
        }
    }

    override suspend fun clearCompletedMusics() {
        coroutineScope {
            //launch { musicRemoteDataSource.clearCompletedMusics() }
            launch { local.clearCompletedMusics() }
        }
    }

    override suspend fun deleteAllMusics() {
        withContext(ioDispatcher) {
            coroutineScope {
                //launch { musicRemoteDataSource.deleteAllMusics() }
                launch { local.deleteAllMusics() }
            }
        }
    }

    override suspend fun deleteMusic(musicId: Long) {
        coroutineScope {
            //launch { musicRemoteDataSource.deleteMusic(musicId) }
            launch { local.deleteMusic(musicId) }
        }
    }

    override suspend fun getPlaylists(): Result<List<Playlist>> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    local.getPlaylists()
                        .also {
                            log.info("RESPONSE ->", it)
                        }
                if (response is Result.Success) {
                    Result.Success(response.data)
                } else
                    Result.Error(
                        DataSourceException(
                            DataSourceException.Error.NULL_EXCEPTION,
                            "Objeto com erro"
                        )
                    )

            } catch (e: SocketTimeoutException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        "NO INTERNET CONNECTION"
                    )
                )
            } catch (e: IOException) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NO_INTERNET_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            } catch (e: Exception) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.UNKNOWN_EXCEPTION,
                        e.message ?: "unknown error"
                    )
                )
            }
        }
    }

    override suspend fun getPlaylist(playlistId: String): Result<Playlist> {
        return try {
            local.getPlaylist(playlistId)
            //Result.Error(DataSourceException(Err(result as Result.Error).exception.toString()))
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }

    override suspend fun savePlaylist(playlist: Playlist): Result<Boolean> {
        return try {
            local.savePlaylist(playlist)
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

    override suspend fun deletePlaylist(playlistId: String): Result<Boolean> {
        return try {
            local.deletePlaylist(playlistId)
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

    private suspend fun getMusicWithId(id: Long): Result<Music>? {
        return local.getMusic(id)
    }
}
