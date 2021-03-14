package com.jeanpaulo.buscador_itunes.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.jeanpaulo.buscador_itunes.model.Artist
import com.jeanpaulo.buscador_itunes.model.Collection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "Music")
data class Music @JvmOverloads constructor(
    @Json(name = "trackId") @PrimaryKey  @ColumnInfo(name = "entryid") val id: Long,

    @Json(name = "trackName") @ColumnInfo(name = "name") val name: String,
    @Json(name = "artworkUrl100") @ColumnInfo(name = "artworkUrl") val artworkUrl: String,

    @Json(name = "artistId") @ColumnInfo(name = "artistId") private val artistId: Long,
    @Json(name = "artistName") @ColumnInfo(name = "artistName") private val artistName: String,

    @Json(name = "collectionId") @ColumnInfo(name = "collectionId") private val collectionId: Long,
    @Json(name = "collectionName") @ColumnInfo(name = "collectionName") private val collectionName: String
) {


    @Ignore
    val artist: Artist = Artist(artistId, artistName)

    @Ignore
    val collection: Collection = Collection(collectionId, collectionName)

}

//REF: CustomAdapter para clases complexas https://gist.github.com/alexforrester/5c96ace4227916fb456ff49a16ef025d
