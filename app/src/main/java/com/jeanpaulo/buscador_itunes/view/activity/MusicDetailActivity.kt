package com.jeanpaulo.buscador_itunes.view.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
import com.jeanpaulo.buscador_itunes.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

import kotlinx.android.synthetic.main.activity_music_detail.*
import java.lang.Exception


class MusicDetailActivity : AppCompatActivity() {

    var appBarExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /*fab_preview.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }*/


        appBar.addOnOffsetChangedListener(object : BaseOnOffsetChangedListener<AppBarLayout>,
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (Math.abs(verticalOffset) > 200) {
                    appBarExpanded = false
                    invalidateOptionsMenu()
                } else {
                    appBarExpanded = true
                    invalidateOptionsMenu()
                }
            }

        })

        val artWorkUrl = intent.getStringExtra(ARTWORK_URL_PARAM)
        Picasso.get().load(artWorkUrl).into(object : com.squareup.picasso.Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                TODO("Not yet implemented")
            }

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                img_collection.setImageBitmap(bitmap)
                if (bitmap != null)
                    setToolbarColor(bitmap)
            }
        })
    }

    private fun setToolbarColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val mutedColor = palette!!.getMutedColor(R.attr.colorPrimary)
            collapseToolbar.setContentScrimColor(mutedColor)
        }
    }

    private lateinit var collapsedMenu: Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_music_detail, menu)
        collapsedMenu = menu
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (collapsedMenu != null
            && (!appBarExpanded || collapsedMenu.size() != 1)
        ) {
            //collapsed
            collapsedMenu.add("Preview")
                .setIcon(android.R.drawable.ic_media_play)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }


    fun getTrackIdParameter() = intent.getLongExtra(TRACK_ID_PARAM, 0L)
    fun setToolbarName(name: String?) {
        collapseToolbar.title = name
    }
}

const val TRACK_ID_PARAM = "track_id"
const val ARTWORK_URL_PARAM = "track_artwork_url"
