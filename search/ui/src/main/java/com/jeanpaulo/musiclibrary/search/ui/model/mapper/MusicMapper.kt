package com.jeanpaulo.musiclibrary.search.ui.model.mapper

import com.jeanpaulo.musiclibrary.search.ui.model.SearchMusicUIModel
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel

fun SearchMusicUIModel.convertToSimpleMusicDetailUIModel() = SimpleMusicDetailUIModel(
    id = this.musicId,
    name = this.musicName,
    artworkUrl = this.artworkUrl
)