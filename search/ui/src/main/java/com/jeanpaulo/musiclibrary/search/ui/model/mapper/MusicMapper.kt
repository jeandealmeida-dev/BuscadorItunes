package com.jeanpaulo.musiclibrary.search.ui.model.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong
import com.jeanpaulo.musiclibrary.search.ui.model.SearchMusicUIModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel

fun SearchMusicUIModel.convertToSimpleMusicDetailUIModel() = SimpleMusicDetailUIModel(
    id = this.musicId,
    name = this.musicName,
    artworkUrl = this.artworkUrl
)

fun SearchMusicUIModel.convertToSong() = MusicPlayerSong(
    id = this.musicId,
    name = this.musicName,
    artist = this.artistName,
    artworkUrl = this.artworkUrl,
    previewUrl = this.previewUrl ?: ""
)