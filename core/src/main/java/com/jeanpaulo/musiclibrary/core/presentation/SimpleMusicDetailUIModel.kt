package com.jeanpaulo.musiclibrary.core.presentation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SimpleMusicDetailUIModel(
    val id: Long,
    val name: String,
    val artworkUrl: String
): Parcelable