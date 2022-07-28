package com.jeanpaulo.buscador_itunes.music.domain

import com.jeanpaulo.buscador_itunes.datasource.IDataSource
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
import kotlinx.coroutines.*
import com.jeanpaulo.buscador_itunes.music.domain.model.util.Result
import com.jeanpaulo.buscador_itunes.music.data.remote.ItunesService
import com.jeanpaulo.buscador_itunes.datasource.local.ILocalDataSource
import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.music.domain.model.Playlist
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.SocketTimeoutException

/**
 * Default implementation of [MusicsRepository]. Single entry point for managing musics' data.
 */
class DefaultMusicInteractor(
    private val remote: ItunesService,
    private val local: ILocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : IDataSource {

    private var log: org.slf4j.Logger = LoggerFactory.getLogger(DefaultMusicInteractor::class.java)

    //REMOTE

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

    override suspend fun lookup(musicId: Long, mediaType: String): Result<Music> {
        return withContext(ioDispatcher) {
            try {

                //TRY TO GET ON  ** LOCAL DATABASE **  FIRST
                val localQuery = local.getMusic(musicId)
                if (localQuery.isSuccessful)
                    return@withContext localQuery

                //IF I DIDNT WORK TRY TO GET ** REMOTE DATABASE **
                val response =
                    remote.lookUp(musicId, mediaType)
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

    //LOCAL

    //C

    override suspend fun saveMusic(music: Music): Result<Long> {
        return withContext(ioDispatcher) {
            try {
                local.saveMusic(music)
            } catch (e: java.lang.Exception) {
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.UNKNOWN_EXCEPTION,
                        e.toString()
                    )
                )
            }
        }
    }

    override suspend fun savePlaylist(playlist: Playlist): Result<Long> {
        return withContext(ioDispatcher) {
            try {
                local.savePlaylist(playlist)
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

    override suspend fun saveMusicInPlaylist(
        music: Music,
        playlistId: Long
    ): Result<Long> {
        return withContext(ioDispatcher) {
            try {
                local.saveMusicInPlaylist(music, playlistId)
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

    override suspend fun saveMusicInFavorites(music: Music): Result<Long> {
        return withContext(ioDispatcher) {
            try {
                local.saveMusicInFavorites(music)
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

    //R

    override suspend fun getMusic(musicId: Long): Result<Music> {
        return withContext(ioDispatcher) {
            try {
                local.getMusic(musicId)
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

    override suspend fun getMusics(): Result<List<Music>> {
        return withContext(ioDispatcher) {
            try {
                local.getMusics()
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

    override suspend fun getPlaylist(playlistId: Long): Result<Playlist> {
        return withContext(ioDispatcher) {
            try {
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
    }

    override suspend fun getPlaylistsFiltered(): Result<List<Playlist>> {
        return withContext(ioDispatcher) {
            try {
                val response =
                    local.getPlaylistsFiltered()
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

    override suspend fun getListMusicInPlaylist(playlistId: Long): Result<List<Music>> {
        return withContext(ioDispatcher) {
            try {
                local.getListMusicInPlaylist(playlistId)
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

    override suspend fun getFavoriteMusics(): Result<List<Music>> {
        return withContext(ioDispatcher) {
            try {
                local.getFavoriteMusics()
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

    override suspend fun getMusicInPlaylist(musicId: Long, playlistId: Long): Result<Music> {
        return withContext(ioDispatcher) {
            try {
                local.getMusicInPlaylist(musicId, playlistId)
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

    override suspend fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
                local.isOnFavoritedPlaylist(dsTrackid)
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


    //U

    //D

    override suspend fun deletePlaylist(playlistId: Long): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
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
    }


    override suspend fun removeMusicFromPlaylist(
        musicId: Long,
        playlistId: Long
    ): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
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
    }


    override suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean> {
        return withContext(ioDispatcher) {
            try {
                local.removeMusicFromFavorites(musicId)
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
