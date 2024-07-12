package com.jeanpaulo.musiclibrary.core.music_player.model

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