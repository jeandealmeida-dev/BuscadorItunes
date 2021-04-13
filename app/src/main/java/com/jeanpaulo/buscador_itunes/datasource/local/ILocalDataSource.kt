package com.jeanpaulo.buscador_itunes.datasource.local

import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.model.util.Result

/**
 * Interface to the data layer.
 */
interface ILocalDataSource {

    //C

    suspend fun saveMusic(music: Music) : Result<Long>

    suspend fun savePlaylist(playlist: Playlist): Result<Long>

    suspend fun saveMusicInPlaylist(music: Music, playlistId: Long): Result<Long>

    suspend fun saveMusicInFavorites(music: Music): Result<Long>

    //R

    suspend fun getMusic(musicId: Long): Result<Music>

    suspend fun getMusics(): Result<List<Music>>

    suspend fun getPlaylist(playlistId: Long): Result<Playlist>

    suspend fun getPlaylistsFiltered(): Result<List<Playlist>>

    suspend fun getListMusicInPlaylist(playlistId: Long): Result<List<Music>>

    suspend fun getFavoriteMusics(): Result<List<Music>>

    suspend fun getMusicInPlaylist(musicId: Long, playlistId: Long): Result<Music>

    suspend fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean>

    //U

    //D

    suspend fun deletePlaylist(playlistId: Long): Result<Boolean>

    suspend fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Result<Boolean>

    suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean>

}