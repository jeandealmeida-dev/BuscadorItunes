package com.jeanpaulo.buscador_itunes.datasource

import com.jeanpaulo.buscador_itunes.datasource.local.ILocalDataSource
import com.jeanpaulo.buscador_itunes.datasource.remote.MusicRemoteDataSource

/**
 * Interface to the data layer.
 */
interface IDataSource : MusicRemoteDataSource, ILocalDataSource{

}