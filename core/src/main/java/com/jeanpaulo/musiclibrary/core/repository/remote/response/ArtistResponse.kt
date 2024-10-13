package com.jeanpaulo.musiclibrary.core.repository.remote.response

import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ArtistResponse(
    @Json(name = "artistId") val artistId: Long? = null,
    @Json(name = "artistName") val artistName: String? = null,
    @Json(name = "primaryGenreName") val primaryGenreName: String? = null,
    @Json(name = "primaryGenreId") val primaryGenreId: Long? = null,
) {
    @Transient
    private var musics = mutableListOf<MusicResponse>()

    fun toModel(): Artist = Artist(
        artistId = artistId ?: -1L,
        name = artistName ?: "",
        primaryGenreName = primaryGenreName,
        primaryGenreId = primaryGenreId,
    ).also { artist ->
        if(musics.isNotEmpty()){
            musics.map { artist.addPopularMusic(it.toModel()) }
        }
    }

    fun addPopularMusic(music: MusicResponse) {
        musics.add(music)
    }
}

