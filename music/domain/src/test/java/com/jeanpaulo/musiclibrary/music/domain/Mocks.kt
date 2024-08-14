package com.jeanpaulo.musiclibrary.music.domain

import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Music

val music1 = Music(musicId = 1, trackName = "Song 1")

val exception = Exception()
val emptyException = EmptyResultException()