package com.jeanpaulo.musiclibrary.core.ui.adapter

import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel

interface SongListListener {
    fun onPressed(music: SongUIModel)

    fun onLongPressed(music: SongUIModel)

    fun onActionPressed(music: SongUIModel)
}