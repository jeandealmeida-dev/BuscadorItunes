package com.jeanpaulo.buscador_itunes.app.music.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
data class SimpleMusicDetailUIModel(
    val id: Long,
    val name: String,
    val artworkUrl: String
): Parcelable