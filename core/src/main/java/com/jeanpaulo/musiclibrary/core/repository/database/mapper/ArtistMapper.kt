package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Artist

fun Artist.toEntity() =
    ArtistEntity(
        artistId = artistId!!,
        name = name,
        country = country,
        primaryGenreName = primaryGenreName
    )