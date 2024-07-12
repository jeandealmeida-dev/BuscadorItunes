package com.jeanpaulo.musiclibrary.core.music_player.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPState(
    val command: String,
    val currentSong: MPSong?,
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
) : Parcelable