package com.jeanpaulo.buscador_itunes.view.music.music_search.music_detail

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.view.music.music_detail.MusicDetailFragmentListener
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import kotlinx.android.synthetic.main.activity_music_detail.*


class MusicDetailActivity : AppCompatActivity(), MusicDetailFragmentListener {

    var appBarExpanded = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupCollapsingBar()
        setupAnimations()
    }

    //ANIMATIONS
    fun setupAnimations() {
        ViewCompat.setTransitionName(
            img_collection,
            VIEW_NAME_HEADER_IMAGE
        );
        ViewCompat.setTransitionName(
            collapseToolbar,
            VIEW_NAME_HEADER_TITLE
        );
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

        val musicName = intent.getStringExtra(MUSIC_NAME_PARAM)
        collapseToolbar.title = musicName
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
        val menuItem: MenuItem = collapsedMenu.findItem(R.id.action_favorite)

        favoriteChecked?.let {
            menuItem.isChecked = favoriteChecked!!
            menuItem.setIcon(if (favoriteChecked!!) R.drawable.ic_star_white_24dp else R.drawable.ic_star_border_white_24dp)
        }
        favoriteVisible?.let { menuItem.setVisible(favoriteVisible!!) }
        return true
    }

    //*** LEGACY CODE FOR EXAMPLE
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (collapsedMenu != null
            && (!appBarExpanded || collapsedMenu.size() != 1)
        ) {
            //collapsed
            //collapsedMenu.add("Preview")
            //    .setIcon(android.R.drawable.ic_media_play)
            //    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else {
            //expanded
        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    lateinit var favoriteAction: () -> Unit

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_favorite -> {
                favoriteAction()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //Better visual transaction
        fab_preview.visibility = GONE
        findViewById<NestedScrollView>(R.id.content).visibility = GONE
        super.onBackPressed()
    }

    //FUNCTIONS CALLED BY FRAGMENT LISTENER

    override fun getTrackIdParameter() = intent.getLongExtra(MUSIC_ID_PARAM, 0L)

    override fun setFavoriteListener(listener: () -> Unit) {
        favoriteAction = listener
    }


    var favoriteChecked: Boolean? = null
    var favoriteVisible: Boolean? = null
    override fun setFavoriteMenuOptions(checked: Boolean, visible: Boolean) {
        favoriteChecked = checked
        favoriteVisible = visible
        invalidateOptionsMenu()
    }

    override fun setTitle(title: String?) {
        collapseToolbar.title = title
    }

    override fun setFabDrawableRes(imageResource: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fab_preview.setImageDrawable(getResources().getDrawable(imageResource, getTheme()))
        } else {
            fab_preview.setImageDrawable(getResources().getDrawable(imageResource))
        }
    }

    override fun setFabVisibility(visible: Boolean) {
        fab_preview.visibility = if (visible) VISIBLE else GONE
    }

    override fun setFabListener(listener: () -> Unit) {
        fab_preview.setOnClickListener {
            listener()
        }
    }
}

//INTENT PARAMS
const val MUSIC_ID_PARAM = "track_id"
const val MUSIC_NAME_PARAM = "track_name"
const val ARTWORK_URL_PARAM = "track_artwork_url"


//ANIMATION

const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
const val VIEW_NAME_HEADER_TITLE = "detail:header:title"


