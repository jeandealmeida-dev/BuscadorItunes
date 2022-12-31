package com.jeanpaulo.musiclibrary.music.ui.model.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.music.ui.model.MusicDetailUIModel

fun Music.convertToMusicUI() = MusicDetailUIModel(
    id = id,
    remoteId = ds_trackId ?: 0L,
    name = trackName ?: "",
    artwork = artworkUrl ?: "",
    previewURL = previewUrl ?: "",
    artist = musicArtist?.name ?: "",
    album = "${musicCollection?.name ?: ""}, ${musicCollection?.id}",
    releaseDate = releaseDate,
    streamable = streamable,
    trackTimeMillis = trackTimeMillis
)