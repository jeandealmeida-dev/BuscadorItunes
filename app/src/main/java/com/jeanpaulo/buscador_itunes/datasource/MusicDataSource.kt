package com.jeanpaulo.buscador_itunes.datasource

import com.jeanpaulo.buscador_itunes.datasource.local.MusicLocalDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.MusicRemoteDataSource

/**
 * Interface to the data layer.
 */
interface MusicDataSource : MusicRemoteDataSource, MusicLocalDataSource{

}