package com.jeanpaulo.musiclibrary.search.domain

import com.jeanpaulo.musiclibrary.core.domain.model.Music


val query = "test"

val music1 = Music(musicId = 1, trackName = "Song 1")
val music2 = Music(musicId = 2, trackName = "Song 2")

val musicList = listOf<Music>(music1, music2)