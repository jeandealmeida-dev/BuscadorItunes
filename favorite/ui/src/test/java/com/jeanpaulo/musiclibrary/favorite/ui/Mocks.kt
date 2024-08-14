package com.jeanpaulo.musiclibrary.favorite.ui

import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel

val music1 = Music(musicId = 1, trackName = "Song 1")
val music2 = Music(musicId = 2, trackName = "Song 2")

val song1 = SongUIModel.fromModel(music1)
val song2 = SongUIModel.fromModel(music2)

val favorite1 = Favorite(music1.musicId).apply { music = music1 }
val favorite2 = Favorite(music2.musicId).apply { music = music2 }

val favoriteList = listOf<Favorite>(favorite1, favorite2)
val songList = listOf<SongUIModel>(song1, song2)