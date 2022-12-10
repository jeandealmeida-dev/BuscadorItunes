package com.jeanpaulo.musiclibrary.core.di

import android.app.Application
import androidx.room.Room
import com.jeanpaulo.musiclibrary.core.repository.database.dao.*
import com.jeanpaulo.musiclibrary.core.repository.database.MusicDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class DatabaseModule {
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun providesRoomDatabase(application: Application): MusicDatabase =
            Room.databaseBuilder(
                application,
                MusicDatabase::class.java,
                "music_library_db"
            )
                .build()

        @JvmStatic
        @Provides
        fun provideMusicDao(musicDatabase: MusicDatabase): MusicDao =
            musicDatabase.musicDao()

        @JvmStatic
        @Provides
        fun provideArtistDao(musicDatabase: MusicDatabase): ArtistDao =
            musicDatabase.artistDao()

        @JvmStatic
        @Provides
        fun provideCollectionDao(musicDatabase: MusicDatabase): CollectionDao =
            musicDatabase.collectionDao()

        @JvmStatic
        @Provides
        fun providePlaylistDao(musicDatabase: MusicDatabase): PlaylistDao =
            musicDatabase.playlistDao()

        @JvmStatic
        @Provides
        fun provideFavoriteDao(musicDatabase: MusicDatabase): FavoriteDao =
            musicDatabase.favoriteDao()

        @JvmStatic
        @Provides
        fun providePlaylistMusicJoinDao(musicDatabase: MusicDatabase): JoinPlaylistMusicDao =
            musicDatabase.playlistMusicJoinDao()
    }
}