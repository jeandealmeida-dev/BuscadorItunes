package com.jeanpaulo.buscador_itunes.music.mvvm.domain

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.music.mvvm.data.LocalDataSource
import com.jeanpaulo.buscador_itunes.favorite.data.FavoriteRepositoryImpl
import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.MusicDatabase
import com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.ItunesService
import com.jeanpaulo.buscador_itunes.music.mvvm.data.remote.RetrofitServiceFactory
import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.PlaylistRepositoryImpl

object ServiceLocator {
    private val lock = Any()
    private var database: MusicDatabase? = null

    @Volatile
    var musicRepository: IDataSource? = null
        @VisibleForTesting set

    fun provideMusicRepository(context: Context): IDataSource {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createMusicRepository(
                    context
                )
        }
    }

    private fun createMusicRepository(context: Context): IDataSource {
        val newRepo =
            DefaultMusicInteractor(
                createRepository(),
                createLocalDataSource(
                    context
                )
            )
        musicRepository = newRepo
        return newRepo
    }

    private fun createRepository(): ItunesService {
        RetrofitServiceFactory()
            .build()
        return RetrofitServiceFactory.retrofit.create(
            ItunesService::class.java
        )
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database
            ?: createDataBase(
                context
            )

        var playlistRepository = PlaylistRepositoryImpl(
            playlistDao = database.playlistDao(),
            playlistWithMusicDao = database.playlistMusicJoinDao()
        )

        var favoriteRepository = FavoriteRepositoryImpl(
            playlistDao = database.playlistDao(),
            playlistWithMusicDao = database.playlistMusicJoinDao(),
            artistDao = database.artistDao(),
            collectionDao = database.collectionDao(),
        )

        return LocalDataSource(
            database.musicDao(),
            database.playlistDao(),
            database.artistDao(),
            database.collectionDao(),
            database.playlistMusicJoinDao(),
            playlistRepository = playlistRepository,
            favoriteRepository = favoriteRepository
        )
    }

    //ROOM

    private fun createDataBase(context: Context): MusicDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            MusicDatabase::class.java, "Music.db"
        ).build()
        database = result
        return result
    }

    /*@VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                FakeTasksRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            musicRepository = null
        }
    }*/
}