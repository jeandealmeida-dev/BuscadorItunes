package com.jeanpaulo.buscador_itunes.music.domain.model

import com.jeanpaulo.buscador_itunes.playlist.data.entity.PlaylistEntity

class Playlist(
    var playlistId: Long = 0,
    var title: String,
    var description: String? = null
) {
    fun toEntity(): PlaylistEntity = PlaylistEntity(playlistId, title, description)
}