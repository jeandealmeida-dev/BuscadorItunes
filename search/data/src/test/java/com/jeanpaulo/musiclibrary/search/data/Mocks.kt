package com.jeanpaulo.musiclibrary.search.data

import com.jeanpaulo.musiclibrary.core.repository.remote.response.MusicResponse
import com.jeanpaulo.musiclibrary.core.repository.remote.response.SearchMusicResponse

val query = "test query"

val musicListResponse = listOf(
    MusicResponse(1L, "Song 1", 1L, "Artist 1"),
    MusicResponse(2L, "Song 2", 2L, "Artist 2")
)

val musicResponse = SearchMusicResponse(
    result = musicListResponse,
    count = musicListResponse.size,
)

val emptyMusicResponse = SearchMusicResponse(
    result = emptyList(),
    count = 0,
)
