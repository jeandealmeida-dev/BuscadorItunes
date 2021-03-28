package com.jeanpaulo.buscador_itunes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.model.Artist
import com.jeanpaulo.buscador_itunes.model.Collection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Music")
class Music(
    @ColumnInfo(name = "trackId") val trackId: Long?,
    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "artworkUrl") val artworkUrl: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: Date?,

    @ColumnInfo(name = "isStreamable") val isStreamable: Boolean?,
    @ColumnInfo(name = "trackTimeMillis") val trackTimeMillis: Long?
) {
    constructor(trackId: Long) : this(trackId, null, null, null, null, null)

    @Ignore
    lateinit var collection: Collection

    @Ignore
    lateinit var artist: Artist

    @PrimaryKey
    var id: Long

    init {
        id = UUID.randomUUID().leastSignificantBits
    }

    val formatedReleaseDate: String
        get() = if (releaseDate != null) SimpleDateFormat("MM-yyyy").format(releaseDate) else "-"

}