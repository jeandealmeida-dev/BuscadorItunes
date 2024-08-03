package com.jeanpaulo.musiclibrary.music.data

import com.jeanpaulo.musiclibrary.commons.exceptions.EmptyResultException
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.domain.model.Collection
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.repository.database.entity.ArtistEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.CollectionEntity
import com.jeanpaulo.musiclibrary.core.repository.database.entity.MusicEntity
import com.jeanpaulo.musiclibrary.core.repository.remote.response.LookUpResponse
import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse

val queryEntity = "song"

val exception = Exception()
val emptyException = EmptyResultException()

val localArtistId = 5L
val localCollectionId = 5L
val localMusicId = 5L

// music1

val music1 = Music(musicId = 1L, trackName = "Song 1")
val artist1 = Artist(artistId = 1L, name = "Artist 1")
val collection1 = Collection(collectionId = 1L, name = "Collection 1")

val music1Entity = MusicEntity.fromModel(music1)
val artist1Entity = ArtistEntity.from(artist1)
val collection1Entity = CollectionEntity.from(collection1)

// music 2

val music2 = Music(musicId = 2L, trackName = "Song 2")
val artist2 = Artist(artistId = 2L, name = "Artist 2")
val collection2 = Collection(collectionId = 2L, name = "Collection 2")

val music2Entity = MusicEntity.fromModel(music2)
val artist2Entity = ArtistEntity.from(artist2)
val collection2Entity = CollectionEntity.from(collection2)

// Remote

val musicResponse1 = MusicResponse(1L, "Song 1", 1L, "Artist 1")
val musicResponse2 = MusicResponse(2L, "Song 2", 2L, "Artist 2")


val musicListResponse = listOf(
    musicResponse1,
    musicResponse2
)

val lookUpResponse = LookUpResponse(
    result = musicListResponse,
    count = musicListResponse.size
)