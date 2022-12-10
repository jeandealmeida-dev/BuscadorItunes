package com.jeanpaulo.buscador_itunes.app.music.presentation.model.mapper

import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.model.SearchMusicUIModel

fun SearchMusicUIModel.convertToSimpleMusicDetailUIModel() = SimpleMusicDetailUIModel(
    id = this.musicId,
    name = this.musicName,
    artworkUrl = this.artworkUrl
)