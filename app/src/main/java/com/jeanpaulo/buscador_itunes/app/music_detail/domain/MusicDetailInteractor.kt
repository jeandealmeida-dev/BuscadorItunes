package com.jeanpaulo.buscador_itunes.app.music_detail.domain

import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.model.MusicDetailUIModel
import com.jeanpaulo.musiclibrary.core.repository.remote.MusicDetailRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface MusicDetailInteractor {
    fun getMusic(musicId: Long, fromRemote: Boolean = false): Single<MusicDetailUIModel>
}

class MusicDetailInteractorImpl @Inject constructor(
    val musicDetailRespository: MusicDetailRepository
) : MusicDetailInteractor {

    override fun getMusic(musicId: Long, fromRemote: Boolean): Single<MusicDetailUIModel> {
        return musicDetailRespository.lookup(musicId, SearchParams.SONG_MEDIA_TYPE).map {
            MusicDetailUIModel(
                id = it.ds_trackId ?: 0L,
                name = it.trackName ?: "",
                artwork = it.artworkUrl ?: "",
                previewURL = it.previewUrl ?: "",
                artist = it.artist?.name ?: "",
                album = it.collection?.name ?: ""
            )
        }
    }
}