package com.jeanpaulo.buscador_itunes.music.mvvm.data

import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.favorite.data.FavoriteRepository
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.PlaylistRepository
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.dao.PlaylistDao
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.dao.PlaylistWithMusicDao
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.Result
import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.dao.ArtistDao
import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.dao.CollectionDao
import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.dao.MusicDao
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Playlist
import com.jeanpaulo.buscador_itunes.util.params.SearchParams


interface ILocalDataSource : PlaylistRepository, FavoriteRepository {

    //C

    suspend fun saveMusic(music: Music) : Result<Long>


    //R

    suspend fun getMusic(musicId: Long): Result<Music>

    suspend fun getMusics(): Result<List<Music>>

    suspend fun getPlaylistsFiltered(): Result<List<Playlist>>

    suspend fun getListMusicInPlaylist(playlistId: Long): Result<List<Music>>



    suspend fun getMusicInPlaylist(musicId: Long, playlistId: Long): Result<Music>


    //U

    //D


}

class LocalDataSource internal constructor(
    private val musicDao: MusicDao,
    private val playlistDao: PlaylistDao,
    private val artistDao: ArtistDao,
    private val collectionDao: CollectionDao,
    private val playlistWithMusicDao: PlaylistWithMusicDao,
    private val playlistRepository: PlaylistRepository,
    private val favoriteRepository: FavoriteRepository,
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


    override suspend fun getPlaylist(playlistId: Long): Result<Playlist> = playlistRepository.getPlaylist(playlistId)
    override suspend fun savePlaylist(playlist: Playlist): Result<Long> = playlistRepository.savePlaylist(playlist)
    override suspend fun saveMusicInPlaylist(music: Music, playlistId: Long): Result<Long> = playlistRepository.saveMusicInPlaylist(music, playlistId)
    override suspend fun removeMusicFromPlaylist(musicId: Long, playlistId: Long): Result<Boolean> = playlistRepository.removeMusicFromPlaylist(musicId, playlistId)
    override suspend fun deletePlaylist(playlistId: Long): Result<Boolean> = playlistRepository.deletePlaylist(playlistId)

    //FAVORITOS
    override suspend fun isOnFavoritedPlaylist(dsTrackid: Long): Result<Boolean> = favoriteRepository.isOnFavoritedPlaylist(dsTrackid)
    override suspend fun saveMusicInFavorites(music: Music): Result<Long> = favoriteRepository.saveMusicInFavorites(music)
    override suspend fun getFavoriteMusics(): Result<List<Music>> = favoriteRepository.getFavoriteMusics()
    override suspend fun removeMusicFromFavorites(musicId: Long): Result<Boolean> =favoriteRepository.removeMusicFromFavorites(musicId)
}
