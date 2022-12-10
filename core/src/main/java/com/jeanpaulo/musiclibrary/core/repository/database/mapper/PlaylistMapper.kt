package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.repository.database.entity.PlaylistEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist

fun Playlist.toEntity(): PlaylistEntity = PlaylistEntity(playlistId, title, description)