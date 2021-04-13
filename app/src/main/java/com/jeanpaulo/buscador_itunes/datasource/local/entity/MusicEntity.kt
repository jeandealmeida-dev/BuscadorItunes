package com.jeanpaulo.buscador_itunes.datasource.local.entity

import androidx.room.*
import com.jeanpaulo.buscador_itunes.model.BaseModel
import com.jeanpaulo.buscador_itunes.model.Music
import java.util.*

@Entity(
    tableName = "music",
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = ["collectionId"],
            childColumns = ["collectionId"]
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = ["artistId"],
            childColumns = ["artistId"]
        )
    ]
)
class MusicEntity(
    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "artworkUrl") val artworkUrl: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: Date?,

    @ColumnInfo(name = "isStreamable") val isStreamable: Boolean?,
    @ColumnInfo(name = "trackTimeMillis") val trackTimeMillis: Long?,
    @ColumnInfo(name = "previewUrl") val previewUrl: String?,

    @PrimaryKey @ColumnInfo(name = "musicId") var musicId: Long?
) {

    @Ignore
    var collection: CollectionEntity? = null
    @ColumnInfo(name = "collectionId")
    var collectionId: Long? = null

    @Ignore
    var artist: ArtistEntity? = null
    @ColumnInfo(name = "artistId")
    var artistId: Long? = null

    fun toModel(): Music = Music(musicId).let { music ->

        music.name = name

        music.artworkUrl = artworkUrl
        music.previewUrl = previewUrl

        music.isStreamable = isStreamable
        music.trackTimeMillis = trackTimeMillis

        music.releaseDate = releaseDate

        music.origin = BaseModel.Origin.LOCAL

        artist?.let { music.artist = it.toModel() }
        collection?.let { music.collection = it.toModel() }

        music
    }
}