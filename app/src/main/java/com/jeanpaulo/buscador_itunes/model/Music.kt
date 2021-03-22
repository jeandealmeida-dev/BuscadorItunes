package com.jeanpaulo.buscador_itunes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.model.Artist
import com.jeanpaulo.buscador_itunes.model.Collection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
@Entity(tableName = "Music")
data class Music(
    @Json(name = "trackId") @ColumnInfo(name = "trackId") val trackId: Long?,

    @Json(name = "trackName") @ColumnInfo(name = "name") val name: String?,
    @Json(name = "artworkUrl100") @ColumnInfo(name = "artworkUrl") val artworkUrl: String?,

    @Json(name = "artistId") @ColumnInfo(name = "artistId") val artistId: Long?,
    @Json(name = "artistName") @ColumnInfo(name = "artistName") val artistName: String?,

    @Json(name = "collectionId") @ColumnInfo(name = "collectionId") val collectionId: Long?,
    @Json(name = "collectionName") @ColumnInfo(name = "collectionName") val collectionName: String?
) {

    @PrimaryKey
    var id: Long

    @Ignore
    val artist: Artist = Artist(artistId, artistName)

    @Ignore
    val collection: Collection = Collection(collectionId, collectionName)

    init {
        id = UUID.randomUUID().leastSignificantBits
    }

}

//REF: CustomAdapter para clases complexas https://gist.github.com/alexforrester/5c96ace4227916fb456ff49a16ef025d
