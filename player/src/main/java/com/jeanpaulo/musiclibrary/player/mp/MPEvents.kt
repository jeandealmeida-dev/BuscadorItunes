package com.jeanpaulo.musiclibrary.player.mp

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