package com.jeanpaulo.buscador_itunes.datasource.local

import com.jeanpaulo.buscador_itunes.datasource.local.dao.*
import com.jeanpaulo.buscador_itunes.datasource.local.entity.PlaylistEntity
import com.jeanpaulo.buscador_itunes.datasource.local.entity.PlaylistMusicJoin
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.model.util.Result
import com.jeanpaulo.buscador_itunes.util.params.SearchParams


/**
 * Concrete implementation of a data source as a db.
 */
class LocalDataSource internal constructor(
    private val musicDao: MusicDao,
    private val playlistDao: PlaylistDao,
    private val artistDao: ArtistDao,
    private val collectionDao: CollectionDao,
    private val playlistWithMusicDao: PlaylistWithMusicDao
) : ILocalDataSource {

    override suspend fun getMusics(): Result<List<Music>> =
        try {
            Result.Success(musicDao.getMusics().map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }

    override suspend fun getMusic(musicId: Long): Result<Music> =
        try {
            val result = musicDao.getMusicById(musicId)
            if (result != null) {

                if (result.artistId != null)
                    result.artist = artistDao.getArtistById(result.artistId!!)
                if (result.collectionId != null)
                    result.collection = collectionDao.getCollectionById(result.collectionId!!)

                Result.Success(result.toModel())
            } else
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NOT_FOUND_EXCEPTION,
                        "ELEMENTO NAO ENCONTRADO"
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

    override suspend fun saveMusic(music: Music): Result<Long> =
        try {
            val musicEntity = music.toEntity()

            if (music.artist != null) {
                val artistEntity = music.artist?.let { it.toEntity() }
                musicEntity.artistId = artistEntity!!.artistId

                artistDao.insertArtist(artistEntity)
            }

            if (music.collection != null) {
                val collectionEntity = music.collection?.let { it.toEntity() }
                musicEntity.collectionId = collectionEntity!!.collectionId

                collectionDao.insertCollection(collectionEntity)
            }

            val musicId = musicDao.insertMusic(musicEntity)
            Result.Success(musicId)
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


    override suspend fun getPlaylistsFiltered(): Result<List<Playlist>> =
        try {
            Result.Success(playlistDao.getPlaylistsFiltered(SearchParams.FAV_PLAYLIST_ID).map { it.toModel() })
        } catch (e: Exception) {
            Result.Error(
                DataSourceException(
                    DataSourceException.Error.UNKNOWN_EXCEPTION,
                    e.toString()
                )
            )
        }


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

    override suspend fun getListMusicInPlaylist(playlistId: Long): Result<List<Music>> =
        try {
            val music = playlistWithMusicDao.getMusicsFromPlaylist(playlistId).map {
                if (it.artistId != null)
                    it.artist = artistDao.getArtistById(it.artistId!!)
                if (it.collectionId != null)
                    it.collection = collectionDao.getCollectionById(it.collectionId!!)

                it.toModel()
            }
            if (music.isNotEmpty())
                Result.Success(music)
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

    override suspend fun getMusicInPlaylist(musicId: Long, playlistId: Long): Result<Music> =
        try {
            val result = playlistWithMusicDao.getMusicInPlaylist(musicId, playlistId)
            if (result != null) {

                if (result.artistId != null)
                    result.artist = artistDao.getArtistById(result.artistId!!)
                if (result.collectionId != null)
                    result.collection = collectionDao.getCollectionById(result.collectionId!!)

                Result.Success(result.toModel())
            } else
                Result.Error(
                    DataSourceException(
                        DataSourceException.Error.NOT_FOUND_EXCEPTION,
                        "ELEMENTO NAO ENCONTRADO"
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

    suspend fun getFavoritedPlaylist(): PlaylistEntity {

        var favoritePlaylist =
            playlistDao.getPlaylistByTitle(SearchParams.FAV_PLAYLIST_ID)?.let {
                if (it.isEmpty()) null else it[0]
            }
        if (favoritePlaylist == null) {
            favoritePlaylist = createFavoritePlaylist()
        }
        return favoritePlaylist!!
    }

    suspend fun createFavoritePlaylist(): PlaylistEntity {
        try {
            val favorite = Playlist()
            favorite.title = SearchParams.FAV_PLAYLIST_ID
            favorite.playlistId = playlistDao.insertPlaylist(favorite.toEntity())
            return favorite.toEntity()
        } catch (e: Exception) {
            throw DataSourceException(DataSourceException.Error.NOT_FOUND_EXCEPTION, e.toString())
        }
    }

    override suspend fun saveMusicInFavorites(music: Music): Result<Long> {
        var favoritePlaylist = getFavoritedPlaylist()
        return saveMusicInPlaylist(music, favoritePlaylist.playlistId)
    }

    override suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean> {
        var favoritePlaylist = getFavoritedPlaylist()
        return removeMusicFromPlaylist(musicId, favoritePlaylist.playlistId)
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
}
