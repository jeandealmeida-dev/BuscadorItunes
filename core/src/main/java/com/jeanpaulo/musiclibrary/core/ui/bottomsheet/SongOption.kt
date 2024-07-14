package com.jeanpaulo.musiclibrary.core.ui.bottomsheet

import com.jeanpaulo.musiclibrary.core.R

enum class SongOption(val id: Int, val desciption: Int, val icon: Int) {
    ADD_FAVORITE(1, R.string.music_option_add_favorite, android.R.drawable.star_off),
    REMOVE_FAVORITE(2, R.string.music_option_remove_favorite, android.R.drawable.star_off),

    ADD_PLAYLIST(3, R.string.music_option_add_playlist, android.R.drawable.star_off),
    GO_TO_ALBUM(4, R.string.music_option_go_to_album, android.R.drawable.star_off),
    GO_TO_ARTIST(5, R.string.music_option_go_to_artist, android.R.drawable.star_off)
}

