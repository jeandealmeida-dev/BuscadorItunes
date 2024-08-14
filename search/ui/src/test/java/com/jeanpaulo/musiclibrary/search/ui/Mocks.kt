package com.jeanpaulo.musiclibrary.search.ui

import androidx.paging.PagingData
import androidx.paging.map
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchState

val query = "test"

val music1 = Music(musicId = 1, trackName = "Song 1")
val music2 = Music(musicId = 2, trackName = "Song 2")

val song1 = SongUIModel.fromModel(music1)
val song2 = SongUIModel.fromModel(music2)

val musicList = listOf<Music>(music1, music2)
val songList = listOf<SongUIModel>(song1, song2)

private fun PagingData<SongUIModel>.toList() : List<SongUIModel> {
    val list = mutableListOf<SongUIModel>()
    this.map {
        list.add(it)
    }
    return list
}

fun equalsState(state1: SearchState, state2: SearchState): Boolean {
    return when {
        state1 is SearchState.Success && state2 is SearchState.Success -> state1.musicList.toList() == state2.musicList.toList()

        state1 is SearchState.Options && state2 is SearchState.Options -> state1.music == state2.music
        else -> state1 == state2
    }
}