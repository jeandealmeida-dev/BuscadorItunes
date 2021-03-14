package com.jeanpaulo.buscador_itunes.repository

import com.jeanpaulo.buscador_itunes.repository.local.MusicLocalDataSource
import com.jeanpaulo.buscador_itunes.repository.remote.MusicRemoteDataSource

/**
 * Interface to the data layer.
 */
interface MusicDataSource : MusicRemoteDataSource, MusicLocalDataSource{

}