package com.jeanpaulo.musiclibrary.player.mp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MPSong(
    val id: Long,
    val name: String,
    val artist: String,
    val artworkUrl: String?,
    val previewUrl: String?,
) : Parcelable