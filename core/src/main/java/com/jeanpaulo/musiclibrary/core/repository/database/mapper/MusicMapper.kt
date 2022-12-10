package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Music

fun Music.toEntity(): MusicEntity = MusicEntity(
    remoteMusicId = ds_trackId,
    musicId = id,
    name = trackName,
    artworkUrl = artworkUrl,
    releaseDate = releaseDate,
    isStreamable = isStreamable,
    trackTimeMillis = trackTimeMillis,
    previewUrl = previewUrl,
)