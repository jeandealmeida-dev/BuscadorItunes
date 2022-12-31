package com.jeanpaulo.musiclibrary.core.repository.database.mapper

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.remote.mapper.convertToMusic

fun MusicEntity.toMusic(): Music {
    val music = Music(
        id = musicId,
        ds_trackId = this.remoteId ?: 0L,
        trackName = this.name ?: "",
        artworkUrl = this.artworkUrl ?: "",
        releaseDate = this.releaseDate,
        trackTimeMillis = this.trackTimeMillis,
        streamable = this.isStreamable,
        previewUrl = this.previewUrl,
    )

    music.musicArtist = Artist(
        id = this.artistId,
        name = this.artist?.name ?: "",
        country = this.artist?.country,
        primaryGenreName = this.artist?.primaryGenreName,
    )

    music.musicCollection = Collection(
        id = this.collectionId,
        name = this.collection?.name ?: ""
    )

    return music
}

fun Music.toEntity(): MusicEntity {
    val music = MusicEntity(
        musicId = this.id,
        remoteId = this.ds_trackId,
        name = this.trackName,
        artworkUrl = this.artworkUrl,
        releaseDate = this.releaseDate,
        isStreamable = this.streamable,
        previewUrl = this.previewUrl,
        trackTimeMillis = this.trackTimeMillis,
        artistId = this.musicArtist?.id ?: 0,
        collectionId = this.musicCollection?.id ?: 0,
    )
    music.artist = this.musicArtist?.toEntity()
    music.collection = this.musicCollection?.toEntity()

    return music
}