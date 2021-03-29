package com.jeanpaulo.buscador_itunes.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

data class Artist(
    @ColumnInfo(name = "artistId") val artistId: Long?,
    @ColumnInfo(name = "artistName") val name: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "primaryGenreName") val primaryGenreName: String?
) {

    @PrimaryKey
    var id: Long

    init {
        id = UUID.randomUUID().leastSignificantBits
    }
}