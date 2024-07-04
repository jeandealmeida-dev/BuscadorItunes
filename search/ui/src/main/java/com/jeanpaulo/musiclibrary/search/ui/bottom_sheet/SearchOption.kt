package com.jeanpaulo.musiclibrary.search.ui.bottom_sheet

import com.jeanpaulo.musiclibrary.core.R

enum class SearchOption(val id: Int, val desciption: Int, val icon: Int) {
    ADD_FAVORITE(1, R.string.music_option_add_favorite, android.R.drawable.star_off),
    ADD_PLAYLIST(2, R.string.music_option_add_playlist, android.R.drawable.star_off),
    GO_TO_ALBUM(3, R.string.music_option_go_to_album, android.R.drawable.star_off),
    GO_TO_ARTIST(4, R.string.music_option_go_to_artist, android.R.drawable.star_off)
}

