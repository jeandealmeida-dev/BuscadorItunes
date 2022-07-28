package com.jeanpaulo.buscador_itunes.datasource

import com.jeanpaulo.buscador_itunes.datasource.local.ILocalDataSource
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.domain.model.util.Result

/**
 * Interface to the data layer.
 */

interface MusicRemoteDataSource {

    suspend fun searchMusic(
        term: String,
        mediaType: String,
        offset: Int,
        limit: Int
    ): Result<List<Music>>

    suspend fun lookup(
        term: Long,
        mediaType: String
    ): Result<Music>
}

interface IDataSource : MusicRemoteDataSource, ILocalDataSource