package com.jeanpaulo.musiclibrary.core.music_player

import com.jeanpaulo.musiclibrary.core.music_player.model.MPSong

abstract class MPEvents<T> {
    abstract fun onPlay()

    abstract fun onPause()
    abstract fun onPlaySong(
        currentSong: T,
        hasNext: Boolean,
        hasPrevious: Boolean,
    )

    abstract fun onStop()

    abstract fun onUpdateCounter(counter: Long)
}