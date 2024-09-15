package com.jeanpaulo.musiclibrary.core.domain.model

data class Artist(
    val artistId: Long,
    val name: String,
    val country: String? = null,
    val primaryGenreName: String? = null,
    val primaryGenreId: Long? = null
) {
    private var recentMusic: MutableList<Music> = mutableListOf()
    val popularMusic: List<Music>
        get() = recentMusic

    fun addPopularMusic(music: Music) {
        recentMusic.add(music)
    }
}