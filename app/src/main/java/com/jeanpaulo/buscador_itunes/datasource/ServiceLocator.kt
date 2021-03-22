package com.jeanpaulo.buscador_itunes.datasource

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.datasource.remote.service.ItunesService
import com.jeanpaulo.buscador_itunes.datasource.local.MusicDatabase
import com.jeanpaulo.buscador_itunes.datasource.local.LocalDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.service.ItunesServiceFactory

object ServiceLocator {
    private val lock = Any()
    private var database: MusicDatabase? = null

    @Volatile
    var musicRepository: MusicDataSource? = null
        @VisibleForTesting set

    fun provideMusicRepository(context: Context): MusicDataSource {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createMusicRepository(
                    context
                )
        }
    }

    private fun createMusicRepository(context: Context): MusicDataSource {
        val newRepo =
            DefaultMusicRepository(
                createRepository(),
                createLocalDataSource(
                    context
                )
            )
        musicRepository = newRepo
        return newRepo
    }

    private fun createRepository(): ItunesService {
        ItunesServiceFactory()
            .build()
        return ItunesServiceFactory.retrofit.create(
            ItunesService::class.java)
    }

    private fun createLocalDataSource(context: Context): LocalDataSource {
        val database = database
            ?: createDataBase(
                context
            )
        return LocalDataSource(
            database.musicDao()
        )
    }

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