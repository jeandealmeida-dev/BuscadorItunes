package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

import com.jeanpaulo.buscador_itunes.music.mvvm.data.local.model.ArtistEntity

class Artist(
    val artistId: Long?,
    val name: String?,
    val country: String?,
    val primaryGenreName: String?
) {
    fun toEntity(): ArtistEntity = ArtistEntity(artistId!!, name, country, primaryGenreName)
}