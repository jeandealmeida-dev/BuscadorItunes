package com.jeanpaulo.buscador_itunes.repository

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource
import com.jeanpaulo.buscador_itunes.repository.local.MusicDatabase
import com.jeanpaulo.buscador_itunes.repository.local.LocalDataSource
import com.jeanpaulo.buscador_itunes.util.RetrofitServiceFactory

object ServiceLocator {
    private val lock = Any()
    private var database: MusicDatabase? = null
    @Volatile
    var musicRepository: MusicRemoteDataSource? = null
        @VisibleForTesting set

    fun provideMusicRepository(context: Context): MusicRemoteDataSource {
        synchronized(this) {
            return musicRepository
                ?: musicRepository
                ?: createMusicRepository(
                    context
                )
        }
    }

    private fun createMusicRepository(context: Context): MusicRemoteDataSource {
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

    private fun createRepository(): MusicRemoteDataSource {
        RetrofitServiceFactory().build()
        return RetrofitServiceFactory.retrofit.create(MusicRemoteDataSource::class.java)
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