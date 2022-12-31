package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import java.util.*

@Entity(
    tableName = MusicEntity.TABLE,
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = [CollectionEntity.ID],
            childColumns = [MusicEntity.COLLECTION_ID]
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = [ArtistEntity.ID],
            childColumns = [MusicEntity.ARTIST_ID]
        )
    ]
)
class MusicEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var musicId: Long = 0,

    @ColumnInfo(name = REMOTE_ID) val remoteId: Long?,
    @ColumnInfo(name = "name") val name: String?,

    @ColumnInfo(name = "artworkUrl") val artworkUrl: String?,
    @ColumnInfo(name = "releaseDate") val releaseDate: Date?,

    @ColumnInfo(name = "isStreamable") val isStreamable: Boolean?,
    @ColumnInfo(name = "trackTimeMillis") val trackTimeMillis: Long?,
    @ColumnInfo(name = "previewUrl") val previewUrl: String?,

    @ColumnInfo(name = COLLECTION_ID) var collectionId: Long = 0,
    @ColumnInfo(name = ARTIST_ID) var artistId: Long = 0
) {

    @Ignore
    var collection: CollectionEntity? = null

    @Ignore
    var artist: ArtistEntity? = null

    companion object {
        const val TABLE = "music"

        const val ID = "id"
        const val REMOTE_ID = "remoteId"
        const val ARTIST_ID = "artistId"
        const val COLLECTION_ID = "collectionId"

        const val T_ID = "$TABLE.$ID"
        const val T_REMOTE_ID = "$TABLE.$REMOTE_ID"
    }

}