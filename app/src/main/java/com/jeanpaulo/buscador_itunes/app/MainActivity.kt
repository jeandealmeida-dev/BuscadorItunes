package com.jeanpaulo.buscador_itunes.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.buscador_itunes.app.music.presentation.MusicActivity
import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.MusicDetailActivity

class MainActivity : AppCompatActivity() {

    val isDebug = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (isDebug) {
            generateIntent_MusicActivity()
        } else {
            val musicId = 1440859107L //
            val artworkUrl =
                "https://is5-ssl.mzstatic.com/image/thumb/Music128/v4/ce/2a/e6/ce2ae6a7-d38c-0c95-7f81-2e958c27daa4/source/100x100bb.jpg"
            generateIntent_MusicDetailActivity(musicId = musicId, artworkUrl = artworkUrl)
        }

        startActivity(intent)
        finish()
    }

    fun generateIntent_MusicActivity() =
        MusicActivity.newInstance(this)

    fun generateIntent_MusicDetailActivity(musicId: Long, artworkUrl: String) =
        MusicDetailActivity.newInstance(this, SimpleMusicDetailUIModel( musicId, "Como as you are", artworkUrl))

}
