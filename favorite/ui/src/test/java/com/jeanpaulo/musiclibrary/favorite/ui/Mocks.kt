package com.jeanpaulo.musiclibrary.favorite.ui

import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.domain.model.Favorite
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.ui.view.FavoriteState

val music1 = Music(musicId = 1, trackName = "Song 1")
val music2 = Music(musicId = 2, trackName = "Song 2")

val song1 = SongUIModel.fromModel(music1)
val song2 = SongUIModel.fromModel(music2)

val favorite1 = Favorite(music1.musicId).apply { music = music1 }
val favorite2 = Favorite(music2.musicId).apply { music = music2 }

val favoriteList = listOf<Favorite>(favorite1, favorite2)
val songList = listOf<SongUIModel>(song1, song2)

fun equalViewState(
    state1: ViewState<List<SongUIModel>>,
    state2: ViewState<List<SongUIModel>>
): Boolean {
    return when {
        state1 is ViewState.Success && state2 is ViewState.Success -> state1.data == state2.data
        else -> state1 == state2
    }
}

fun equalState(state1: FavoriteState, state2: FavoriteState): Boolean {
    return when {
        state1 is FavoriteState.Wrapper && state2 is FavoriteState.Wrapper -> equalViewState(
            state1.viewState,
            state2.viewState
        )

        else -> state1 == state2
    }
}