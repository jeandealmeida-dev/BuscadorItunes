package com.jeanpaulo.buscador_itunes.music.data.local.model

import androidx.room.*
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
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

    fun toModel(): Music = Music(
        ds_trackId = musicId,
        trackName = name,
        artworkUrl = artworkUrl,
        previewUrl = previewUrl,
        trackTimeMillis = trackTimeMillis,
        releaseDate = releaseDate,
        streamable = isStreamable
    ).let { music ->
        artist?.let { music.artist = it.toModel() }
        collection?.let { music.collection = it.toModel() }
        music
    }
}