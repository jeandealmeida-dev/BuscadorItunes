/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jeanpaulo.buscador_itunes.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jeanpaulo.buscador_itunes.datasource.local.dao.*
import com.jeanpaulo.buscador_itunes.datasource.local.entity.*
import com.jeanpaulo.buscador_itunes.datasource.local.util.DateConverter

/**
 * The Room Database that contains the Task table.
 *
 * Note that exportSchema should be true in production databases.
 */
@Database(
    entities = [MusicEntity::class, ArtistEntity::class, CollectionEntity::class, PlaylistEntity::class, PlaylistMusicJoin::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class MusicDatabase : RoomDatabase() {

    abstract fun musicDao(): MusicDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun artistDao(): ArtistDao

    abstract fun collectionDao(): CollectionDao

    abstract fun playlistMusicJoinDao(): PlaylistWithMusicDao
}
