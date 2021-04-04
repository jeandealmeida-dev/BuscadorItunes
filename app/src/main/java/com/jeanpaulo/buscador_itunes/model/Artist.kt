package com.jeanpaulo.buscador_itunes.model

class Artist() {

    constructor(
        artistId: Long?,
        name: String?,
        country: String?,
        primaryGenreName: String?
    ) : this() {
        this.artistId = artistId
        this.name = name
        this.country = country
        this.primaryGenreName = primaryGenreName
    }

    var artistId: Long? = null
    var name: String? = null
    var country: String? = null
    var primaryGenreName: String? = null
}