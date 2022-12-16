package com.jeanpaulo.musiclibrary.search.presentation.model.mapper

import com.jeanpaulo.musiclibrary.search.presentation.model.SearchMusicUIModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel

fun SearchMusicUIModel.convertToSimpleMusicDetailUIModel() = SimpleMusicDetailUIModel(
    id = this.musicId,
    name = this.musicName,
    artworkUrl = this.artworkUrl
)