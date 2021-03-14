package com.jeanpaulo.buscador_itunes.repository.local

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.repository.local.LocalDataSource

/**
 * Interface to the data layer.
 */
interface MusicLocalDataSource {

    fun observeMusics(): LiveData<Result<List<Music>>>

    suspend fun getMusics(): Result<List<Music>>

    suspend fun refreshMusics()

    fun observeMusic(musicId: Long): LiveData<Result<Music>>?

    suspend fun getMusic(musicId: Long): Result<Music>?

    suspend fun refreshMusic(musicId: Long)

    suspend fun saveMusic(music: Music)

    suspend fun completeMusic(music: Music)

    suspend fun completeMusic(musicId: Long)

    suspend fun activateMusic(music: Music)

    suspend fun activateMusic(musicId: Long)

    suspend fun clearCompletedMusics()

    suspend fun deleteAllMusics()

    suspend fun deleteMusic(musicId: Long)
}