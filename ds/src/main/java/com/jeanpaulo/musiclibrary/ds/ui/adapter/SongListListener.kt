package com.jeanpaulo.musiclibrary.ds.ui.adapter

import com.jeanpaulo.musiclibrary.ds.ui.model.SongUIModel

interface SongListListener {
    fun onPressed(music: SongUIModel)

    fun onLongPressed(music: SongUIModel)

    fun onActionPressed(music: SongUIModel)
}