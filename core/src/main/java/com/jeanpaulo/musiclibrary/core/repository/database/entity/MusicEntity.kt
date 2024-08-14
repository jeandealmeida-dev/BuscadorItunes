package com.jeanpaulo.musiclibrary.core.repository.database.entity

import androidx.room.*
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import java.util.*

@Entity(
    tableName = MusicEntity.TABLE,
    foreignKeys = [
        ForeignKey(
            entity = CollectionEntity::class,
            parentColumns = [CollectionEntity.COLLECTION_ID],
            childColumns = [MusicEntity.COLLECTION_ID]
        ),
        ForeignKey(
            entity = ArtistEntity::class,
            parentColumns = [ArtistEntity.ARTIST_ID],
            childColumns = [MusicEntity.ARTIST_ID]
        )
    ]
)
class MusicEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = ID) var id: Long = 0,

    @ColumnInfo(name = MUSIC_ID) val musicId: Long,
    @ColumnInfo(name = NAME) val name: String,

    @ColumnInfo(name = ARTWORK_URL) val artworkUrl: String?,
    @ColumnInfo(name = RELEASE_DATE) val releaseDate: Date?,

    @ColumnInfo(name = IS_STREAMABLE) val isStreamable: Boolean?,
    @ColumnInfo(name = TRACK_TIME) val trackTimeMillis: Long?,
    @ColumnInfo(name = PREVIEW_URL) val previewUrl: String?,

    @ColumnInfo(name = COLLECTION_ID) var collectionId: Long = 0,
    @ColumnInfo(name = ARTIST_ID) var artistId: Long = 0
) {

    @Ignore
    var collection: CollectionEntity? = null

    @Ignore
    var artist: ArtistEntity? = null

    fun toModel() = Music(
        id = id,
        musicId = musicId,
        trackName = name,
        artworkUrl = artworkUrl,
        releaseDate = releaseDate,
        streamable = isStreamable,
        trackTimeMillis = trackTimeMillis,
        previewUrl = previewUrl,
    ).also {
        it.musicCollection = collection?.toModel()
        it.musicArtist = artist?.toModel()
    }

    companion object {
        const val TABLE = "music"
        const val ID = "id"

        const val MUSIC_ID = "musicId"
        const val ARTIST_ID = "artistId"
        const val COLLECTION_ID = "collectionId"

        const val NAME = "name"
        const val ARTWORK_URL = "artworkUrl"
        const val RELEASE_DATE = "releaseDate"
        const val IS_STREAMABLE = "isStreamable"
        const val TRACK_TIME = "trackTimeMillis"
        const val PREVIEW_URL = "previewUrl"

        const val T_ID = "$TABLE.$ID"
        const val T_REMOTE_ID = "$TABLE.$MUSIC_ID"

        fun fromModel(music: Music) = MusicEntity(
            id = music.id,
            musicId = music.musicId,
            name = music.trackName,
            artworkUrl = music.artworkUrl,
            releaseDate = music.releaseDate,
            isStreamable = music.streamable,
            trackTimeMillis = music.trackTimeMillis,
            previewUrl = music.previewUrl,

            artistId = music.musicArtist?.artistId ?: 0,
            collectionId = music.musicCollection?.collectionId ?: 0,
        )
    }
}