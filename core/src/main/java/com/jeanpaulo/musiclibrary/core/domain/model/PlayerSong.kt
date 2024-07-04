package com.jeanpaulo.musiclibrary.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MusicPlayerSong(
    val id: Long,
    val name: String,
    val artist: String,
    val artworkUrl: String,
    val previewUrl: String
) : Parcelable