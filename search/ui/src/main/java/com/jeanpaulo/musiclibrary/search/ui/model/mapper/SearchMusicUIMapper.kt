package com.jeanpaulo.musiclibrary.search.ui.model.mapper

import com.jeanpaulo.musiclibrary.search.domain.model.SearchMusic
import com.jeanpaulo.musiclibrary.search.ui.model.SearchMusicUIModel


fun SearchMusic.toUIModel() =
    SearchMusicUIModel(
        musicId = this.musicId,
        musicName = this.musicName,
        artworkUrl = this.artworkUrl,
        artistName = this.artistName,
        collectionName = this.collectionName,
        collectionYear = this.collectionYear
    )