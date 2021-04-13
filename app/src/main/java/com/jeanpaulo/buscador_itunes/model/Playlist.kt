package com.jeanpaulo.buscador_itunes.model

import com.jeanpaulo.buscador_itunes.datasource.local.entity.PlaylistEntity

class Playlist() {

    constructor(playlistId: Long?) : this() {
        this.playlistId = playlistId
    }

    var playlistId: Long? = null
    var title: String? = null
    var description: String? = null

    fun toEntity(): PlaylistEntity =
        if (playlistId != null) PlaylistEntity(title!!, description, playlistId!!)
        else PlaylistEntity(title!!, description)


    val checkValid
        get() = title == null || title!!.isEmpty()

    val isNewObject
        get() = playlistId == null
}