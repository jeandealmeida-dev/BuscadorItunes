package com.jeanpaulo.buscador_itunes.music.mvvm.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.buscador_itunes.music.music_detail.ARTWORK_URL_PARAM
import com.jeanpaulo.buscador_itunes.music.music_detail.MUSIC_ID_PARAM
import com.jeanpaulo.buscador_itunes.music.music_detail.MUSIC_NAME_PARAM
import com.jeanpaulo.buscador_itunes.music.music_detail.MusicDetailActivity
import com.jeanpaulo.buscador_itunes.music.mvvm.presentation.MusicActivity

class MainActivity : AppCompatActivity() {

    val isProductionVersion = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (isProductionVersion) generateIntent_MusicActivity()
        else {
            val musicId = 1440859107L //
            val artworkUrl =
                "https://is5-ssl.mzstatic.com/image/thumb/Music128/v4/ce/2a/e6/ce2ae6a7-d38c-0c95-7f81-2e958c27daa4/source/100x100bb.jpg"
            generateIntent_MusicDetailActivity(musicId = musicId, artworkUrl = artworkUrl)
        }

        startActivity(intent)
        finish()
    }

    fun generateIntent_MusicActivity(): Intent {
        val intent = Intent(this, MusicActivity::class.java)
        val b = Bundle()
        intent.putExtras(b)
        return intent
    }

    fun generateIntent_MusicDetailActivity(musicId: Long, artworkUrl: String): Intent {
        val intent = Intent(this, MusicDetailActivity::class.java)
        val b = Bundle()
        b.putLong(MUSIC_ID_PARAM, musicId) //Your id
        b.putString(MUSIC_NAME_PARAM, "music_name") //Your id
        b.putString(ARTWORK_URL_PARAM, artworkUrl) //Your id
        intent.putExtras(b)
        return intent
    }
}
