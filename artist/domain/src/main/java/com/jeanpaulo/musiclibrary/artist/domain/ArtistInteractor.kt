package com.jeanpaulo.musiclibrary.artist.domain

import com.jeanpaulo.musiclibrary.artist.data.ArtistRepository
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

interface ArtistInteractor {
    fun getArtist(id: Long): Single<Artist>
}

class ArtistInteractorImpl @Inject constructor(
    private val repository: ArtistRepository
) : ArtistInteractor {

    override fun getArtist(id: Long): Single<Artist> =
        repository.getArtist(id).map {
            it.toModel()
        }
}
