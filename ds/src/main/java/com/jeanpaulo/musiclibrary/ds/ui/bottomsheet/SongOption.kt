package com.jeanpaulo.musiclibrary.ds.ui.bottomsheet

import com.jeanpaulo.musiclibrary.ds.R

enum class SongOption(val id: Int, val desciption: Int, val icon: Int) {
    ADD_FAVORITE(1, R.string.music_option_add_favorite, R.drawable.ic_star_border),
    REMOVE_FAVORITE(2, R.string.music_option_remove_favorite, R.drawable.ic_star_filled),
}

