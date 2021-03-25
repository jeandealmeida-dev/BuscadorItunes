package com.jeanpaulo.buscador_itunes.view.activity

import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.buscador_itunes.R

import kotlinx.android.synthetic.main.activity_music_detail.*

class MusicDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fab_preview.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

    }

    fun getTrackIdParameter() = intent.getLongExtra(TRACK_ID_PARAM, 0L)
    fun getArtworkUrlParameter() = intent.getStringExtra(ARTWORK_URL_PARAM)

}

const val TRACK_ID_PARAM = "track_id"
const val ARTWORK_URL_PARAM = "track_artwork_url"
