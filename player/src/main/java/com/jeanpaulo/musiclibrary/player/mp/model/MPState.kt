package com.jeanpaulo.musiclibrary.player.mp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPState(
    val command: String,
    val currentSong: MPSong?,
    val hasPrevious: Boolean = false,
    val hasNext: Boolean = false,
) : Parcelable