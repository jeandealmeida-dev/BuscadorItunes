package com.jeanpaulo.buscador_itunes.datasource.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.model.Artist
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model._Collection
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "Music")
class MusicEntity(
    @ColumnInfo(name = "trackId") val ds_trackId: Long?,
    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "artworkUrl") val artworkUrl: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: Date?,

    @ColumnInfo(name = "isStreamable") val isStreamable: Boolean?,
    @ColumnInfo(name = "trackTimeMillis") val trackTimeMillis: Long?,
    @ColumnInfo(name = "previewUrl") val previewUrl: String?,

    @PrimaryKey val musicId: String = UUID.randomUUID().toString()
) {

    fun toModel(): Music = Music(musicId).let {

        it.name = name
        it.ds_trackId = ds_trackId

        it.artworkUrl = artworkUrl
        it.previewUrl = previewUrl

        it.isStreamable = isStreamable
        it.trackTimeMillis = trackTimeMillis

        it.releaseDate = releaseDate

        it
    }

}