package com.jeanpaulo.buscador_itunes.music.mvvm.domain.model

import com.jeanpaulo.buscador_itunes.playlist.mvvm.data.entity.PlaylistEntity

class Playlist(
    var playlistId: Long = 0,
    var title: String,
    var description: String? = null
) {
    fun toEntity(): PlaylistEntity = PlaylistEntity(playlistId, title, description)
}