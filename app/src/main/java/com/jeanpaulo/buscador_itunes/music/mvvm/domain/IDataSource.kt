package com.jeanpaulo.buscador_itunes.music.mvvm.domain

import com.jeanpaulo.buscador_itunes.music.mvvm.data.ILocalDataSource
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.Result

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