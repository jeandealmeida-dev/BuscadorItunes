package com.jeanpaulo.buscador_itunes.repository

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.Music
import kotlinx.coroutines.*
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource
import com.jeanpaulo.buscador_itunes.repository.local.MusicLocalDataSource
import com.jeanpaulo.buscador_itunes.repository.remote.util.ItunesResponse
import com.jeanpaulo.buscador_itunes.repository.remote.util.ItunesResponse2
import com.jeanpaulo.buscador_itunes.util.wrapEspressoIdlingResource
import io.reactivex.Single

/**
 * Default implementation of [MusicsRepository]. Single entry point for managing musics' data.
 */
class DefaultMusicRepository(
    private val musicRemoteDataSource: MusicRemoteDataSource,
    private val musicLocalDataSource: MusicLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MusicDataSource {

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

    override fun searchMusic(
        term: String,
        mediaType: String,
        offset: Int,
        limit: Int
    ): Single<ItunesResponse> {
        //TODO salvar lista de pesquisa na base de dados
        return musicRemoteDataSource.searchMusic(term, mediaType, offset, limit)
    }

    override fun getCollection(term: Long, mediaType: String): ItunesResponse2 {
        //TODO salvar em cache itens ja acessados
        return musicRemoteDataSource.getCollection(term, mediaType)
    }


    override fun observeMusics(): LiveData<Result<List<Music>>> {
        return musicLocalDataSource.observeMusics()
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
        return musicLocalDataSource.observeMusic(musicId)
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
            launch { musicLocalDataSource.saveMusic(music) }
        }
    }

    override suspend fun completeMusic(music: Music) {
        coroutineScope {
            //launch { musicRemoteDataSource.completeMusic(music) }
            launch { musicLocalDataSource.completeMusic(music) }
        }
    }

    override suspend fun completeMusic(musicId: Long) {
        withContext(ioDispatcher) {
            (getMusicWithId(musicId) as? Result.Success)?.let { it ->
                completeMusic(it.data)
            }
        }
    }

    override suspend fun activateMusic(music: Music) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            //launch { musicRemoteDataSource.activateMusic(music) }
            launch { musicLocalDataSource.activateMusic(music) }
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
            launch { musicLocalDataSource.clearCompletedMusics() }
        }
    }

    override suspend fun deleteAllMusics() {
        withContext(ioDispatcher) {
            coroutineScope {
                //launch { musicRemoteDataSource.deleteAllMusics() }
                launch { musicLocalDataSource.deleteAllMusics() }
            }
        }
    }

    override suspend fun deleteMusic(musicId: Long) {
        coroutineScope {
            //launch { musicRemoteDataSource.deleteMusic(musicId) }
            launch { musicLocalDataSource.deleteMusic(musicId) }
        }
    }

    private suspend fun getMusicWithId(id: Long): Result<Music>? {
        return musicLocalDataSource.getMusic(id)
    }
}
