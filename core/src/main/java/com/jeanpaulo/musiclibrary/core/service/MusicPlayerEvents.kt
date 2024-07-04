package com.jeanpaulo.musiclibrary.core.service

import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong

abstract class MusicPlayerEvents {
    abstract fun onPlay()
    abstract fun onPlaySong(song: MusicPlayerSong)
    abstract fun onPause()
    abstract fun onNext()
    abstract fun onStop()
    abstract fun onPrevious()
}