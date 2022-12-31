package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Artist

fun Artist.toEntity() : ArtistEntity =
    ArtistEntity(
        artistId = id,
        name = name,
        country = country,
        primaryGenreName = primaryGenreName
    )

fun ArtistEntity.toModel() : Artist =
    Artist(
        id = artistId,
        name = name,
        country = country,
        primaryGenreName = primaryGenreName
    )