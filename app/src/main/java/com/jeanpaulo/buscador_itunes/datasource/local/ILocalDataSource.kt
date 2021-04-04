package com.jeanpaulo.buscador_itunes.datasource.local

import androidx.lifecycle.LiveData
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.model.util.Result

/**
 * Interface to the data layer.
 */
interface ILocalDataSource {

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

    suspend fun getPlaylists(): Result<List<Playlist>>

    suspend fun getPlaylist(playlistId: String): Result<Playlist>

    suspend fun savePlaylist(playlist: Playlist): Result<Boolean>

    suspend fun deletePlaylist(playlistId: String): Result<Boolean>
}