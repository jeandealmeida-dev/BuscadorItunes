package com.jeanpaulo.buscador_itunes.datasource.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.jeanpaulo.buscador_itunes.model.Music
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.jeanpaulo.buscador_itunes.model.util.Result


/**
 * Concrete implementation of a data source as a db.
 */
class LocalDataSource internal constructor(
    private val musicDao: MusicDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MusicLocalDataSource {

    override fun observeMusics(): LiveData<Result<List<Music>>> {
        return musicDao.observeMusics().map {
            Result.Success(it)
        }
    }

    override suspend fun getMusics(): Result<List<Music>> {
        return try {
            Result.Success(musicDao.getMusics())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun observeMusic(taskId: Long): LiveData<Result<Music>> {
        return musicDao.observeMusicById(taskId).map {
            Result.Success(it)
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
            val task = musicDao.getMusicById(musicId)
            if (task != null) {
                return@withContext Result.Success(task)
            } else {
                return@withContext Result.Error(Exception("Music not found!"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun saveMusic(task: Music) = withContext(ioDispatcher) {
        musicDao.insertMusic(task)
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
}
