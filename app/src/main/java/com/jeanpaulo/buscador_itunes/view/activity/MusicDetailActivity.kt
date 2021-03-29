package com.jeanpaulo.buscador_itunes.view.activity

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
import com.jeanpaulo.buscador_itunes.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_music_detail.*


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


        setupCollapsingBar()
        setupAnimations()
    }

    //ANIMATIONS
    fun setupAnimations() {
        ViewCompat.setTransitionName(img_collection, VIEW_NAME_HEADER_IMAGE);
        ViewCompat.setTransitionName(collapseToolbar, VIEW_NAME_HEADER_TITLE);
    }


    //FAB
    fun setFABListener(listener: () -> Unit) {
        fab_preview.visibility = View.VISIBLE
        fab_preview.setOnClickListener {
            listener()
        }
    }


    fun onChangedPlayerState(playing: Boolean) {
        val imageResource =
            if (playing) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab_preview.setImageDrawable(getResources().getDrawable(imageResource, getTheme()))
        } else {
            fab_preview.setImageDrawable(getResources().getDrawable(imageResource))
        }
    }

    //COLLAPSION TOOLBAR

    private fun setupCollapsingBar() {
        appBar.addOnOffsetChangedListener(object : BaseOnOffsetChangedListener<AppBarLayout>,
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (Math.abs(verticalOffset) > 300) {
                    appBarExpanded = false
                    invalidateOptionsMenu()
                } else {
                    appBarExpanded = true
                    invalidateOptionsMenu()
                }
            }

        })

        val artWorkUrl = intent.getStringExtra(ARTWORK_URL_PARAM)
        Picasso.get().load(artWorkUrl).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

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

    //MENU FUNCTIONS

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //FUNCTIONS CALLED BY FRAGMENT


    fun getTrackIdParameter() = intent.getLongExtra(TRACK_ID_PARAM, 0L)
    fun setToolbarName(name: String?) {
        collapseToolbar.title = name
    }

}

//INTENT PARAMS
const val TRACK_ID_PARAM = "track_id"
const val ARTWORK_URL_PARAM = "track_artwork_url"


//ANIMATION

const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
const val VIEW_NAME_HEADER_TITLE = "detail:header:title"


