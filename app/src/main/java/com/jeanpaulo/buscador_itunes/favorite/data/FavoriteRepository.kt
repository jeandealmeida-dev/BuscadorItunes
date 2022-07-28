package com.jeanpaulo.buscador_itunes.favorite

import com.jeanpaulo.buscador_itunes.music.data.local.dao.ArtistDao
import com.jeanpaulo.buscador_itunes.music.data.local.dao.CollectionDao
import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.domain.model.Playlist
import com.jeanpaulo.buscador_itunes.music.domain.model.util.Result
import com.jeanpaulo.buscador_itunes.playlist.data.PlaylistMusicJoin
import com.jeanpaulo.buscador_itunes.playlist.data.dao.PlaylistDao
import com.jeanpaulo.buscador_itunes.playlist.data.dao.PlaylistWithMusicDao
import com.jeanpaulo.buscador_itunes.playlist.data.entity.PlaylistEntity
import com.jeanpaulo.buscador_itunes.util.params.SearchParams

interface FavoriteRepository {
    suspend fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean>

    suspend fun saveMusicInFavorites(music: Music): Result<Long>

    suspend fun getFavoriteMusics(): Result<List<Music>>

    suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean>
}

class FavoriteRepositoryImpl(
    private val artistDao: ArtistDao,
    private val collectionDao: CollectionDao,
    private val playlistDao: PlaylistDao,
    private val playlistWithMusicDao: PlaylistWithMusicDao,
) : FavoriteRepository {


    override suspend fun saveMusicInFavorites(music: Music): Result<Long> {
        var favoritePlaylist =
            playlistDao.getPlaylistByTitle(SearchParams.FAV_PLAYLIST_ID)?.let {
                if (it.isEmpty()) null else it[0]
            }
        if (favoritePlaylist == null) {
            favoritePlaylist = createFavoritePlaylist()
        }

        try {
            val playlistMusicJoin = PlaylistMusicJoin(favoritePlaylist.playlistId, music.id!!)
            val id = playlistWithMusicDao.insert(playlistMusicJoin);
            return Result.Success(id)
        } catch (e: Exception) {
            return Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }
    }

    override suspend fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean> {
        try {
            val favoritePlaylist = getFavoritedPlaylist()
            val resultList: List<PlaylistMusicJoin> =
                playlistWithMusicDao.getMusicOnPlaylist(dsTrackid, favoritePlaylist.playlistId)
            return Result.Success(resultList.isNotEmpty())
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getFavoriteMusics(): Result<List<Music>> {
        val favoritePlaylist = getFavoritedPlaylist()
        val listMusicFavoritePlaylist =
            playlistWithMusicDao.getMusicsFromPlaylist(favoritePlaylist.playlistId!!)
                .map {
                    if (it.artistId != null)
                        it.artist = artistDao.getArtistById(it.artistId!!)
                    if (it.collectionId != null)
                        it.collection = collectionDao.getCollectionById(it.collectionId!!)

                    it.toModel()
                }

        return Result.Success(listMusicFavoritePlaylist)
    }

    override suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean> {
        var favoritePlaylist = getFavoritedPlaylist()
        return try {
            playlistWithMusicDao.removeMusicFromPlaylist(musicId, favoritePlaylist.playlistId)
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

    private suspend fun createFavoritePlaylist(): PlaylistEntity {
        try {
            val favorite = Playlist(
                title = SearchParams.FAV_PLAYLIST_ID
            )
            favorite.playlistId = playlistDao.insertPlaylist(favorite.toEntity())
            return favorite.toEntity()
        } catch (e: Exception) {
            throw DataSourceException(DataSourceException.Error.NOT_FOUND_EXCEPTION, e.toString())
        }
    }

    private suspend fun getFavoritedPlaylist(): PlaylistEntity {

        var favoritePlaylist =
            playlistDao.getPlaylistByTitle(SearchParams.FAV_PLAYLIST_ID)?.let {
                if (it.isEmpty()) null else it[0]
            }
        if (favoritePlaylist == null) {
            favoritePlaylist = createFavoritePlaylist()
        }
        return favoritePlaylist!!
    }
}