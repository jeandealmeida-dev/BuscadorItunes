package com.jeanpaulo.buscador_itunes.app.music_detail.presentation

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.app.music.presentation.model.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.buscador_itunes.databinding.ActivityMusicDetailBinding
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.viewmodel.MusicDetailViewModel
import com.jeanpaulo.buscador_itunes.app.music_detail.presentation.viewmodel.MusicPlayerState
import com.jeanpaulo.musiclibrary.commons.extras.MyMediaPlayer
import com.jeanpaulo.musiclibrary.commons.extensions.gone
import com.jeanpaulo.musiclibrary.commons.extensions.replace
import com.jeanpaulo.musiclibrary.commons.extensions.visible
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target


class MusicDetailActivity : BaseMvvmActivity() {

    private val vm by appViewModel<MusicDetailViewModel>()
    private var _binding: ActivityMusicDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var myMediaPlayer: MyMediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMusicDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(vm)

        setupListeners()
        setupWidgets()
        setupAnimations()
        setupMusicDetailContainer()
    }

    fun setupListeners(){
        vm.musicPlayerState.observe(this){ playerState ->
            when(playerState){
                is MusicPlayerState.Setup -> {
                    myMediaPlayer = MyMediaPlayer(this, playerState.uri)
                    binding.fabPreview.visible()
                }
                MusicPlayerState.Stop -> {
                    myMediaPlayer.stop()
                    binding.fabPreview.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_play))
                }

                MusicPlayerState.Play -> {
                    myMediaPlayer.play()
                    binding.fabPreview.setImageDrawable(ContextCompat.getDrawable(this, android.R.drawable.ic_media_pause))
                }
            }
        }
    }

    fun setupWidgets(){
        setupCollapsingToolbar()
        setupFAB()
    }

    //COLLAPSION TOOLBAR
    var appBarExpanded = true
    private fun setupCollapsingToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.appBar.addOnOffsetChangedListener(object : BaseOnOffsetChangedListener<AppBarLayout>,
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

        Picasso.with(binding.root.context).load(vm.music.artworkUrl).into(object : Target {
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.imgCollection.setImageBitmap(bitmap)
                if (bitmap != null)
                    setToolbarColor(bitmap)
            }

            override fun onBitmapFailed(errorDrawable: Drawable?) {
                TODO("Not yet implemented")
            }
        })

        binding.collapseToolbar.title = vm.music.name
    }

    private fun setToolbarColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val mutedColor = palette!!.getMutedColor(R.attr.colorPrimary)
            binding.collapseToolbar.setContentScrimColor(mutedColor)
        }
    }

    //MENU FUNCTIONS

    private lateinit var collapsedMenu: Menu
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_music_detail, menu)
        collapsedMenu = menu
        val menuItem: MenuItem = collapsedMenu.findItem(R.id.action_favorite)

//        favoriteChecked?.let { favorite ->
//            menuItem.isChecked = favorite
//            menuItem.setIcon(if (favorite) R.drawable.ic_star_white_24dp else R.drawable.ic_star_border_white_24dp)
        //}
        //favoriteVisible?.let { menuItem.setVisible(it) }
        return true
    }

    //ANIMATIONS
    fun setupAnimations() {
        ViewCompat.setTransitionName(
            binding.imgCollection,
            VIEW_NAME_HEADER_IMAGE
        );
        ViewCompat.setTransitionName(
            binding.collapseToolbar,
            VIEW_NAME_HEADER_TITLE
        );
    }

    //*** LEGACY CODE FOR EXAMPLE
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (!appBarExpanded || collapsedMenu.size() != 1) {
            collapsedMenu.add("Preview")
                .setIcon(android.R.drawable.ic_media_play)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else {

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
        binding.fabPreview.gone()
        binding.content.gone()
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        binding.fabPreview.gone()
        myMediaPlayer.stop()
        _binding = null
    }

    fun setupMusicDetailContainer(){
        supportFragmentManager.replace(
            binding.fragment.id,
            MusicDetailFragment.newInstance(),
            tag = MusicDetailFragment::class.simpleName
        )
    }

//    var favoriteChecked: Boolean? = null
//    var favoriteVisible: Boolean? = null
//    override fun setFavoriteMenuOptions(checked: Boolean, visible: Boolean) {
//        favoriteChecked = checked
//        favoriteVisible = visible
//        invalidateOptionsMenu()
//    }
//
//    override fun setFabDrawableRes(imageResource: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            binding.fabPreview.setImageDrawable(resources.getDrawable(imageResource, theme))
//        } else {
//            binding.fabPreview.setImageDrawable(resources.getDrawable(imageResource))
//        }
//    }
//
//    override fun setFabVisibility(visible: Boolean) {
//        binding.fabPreview.visibility = if (visible) VISIBLE else GONE
//    }
//

    fun setupFAB(){
        binding.fabPreview.setOnClickListener {
            vm.changePlayerState()
        }
    }

    companion object {

        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"

        const val MUSIC_ID_PARAM = "track_id"
        const val MUSIC_PARAM = "music_param"
        const val ARTWORK_URL_PARAM = "track_artwork_url"

        fun newInstance(context: Context, music: SimpleMusicDetailUIModel) : Intent{
            return Intent(context, MusicDetailActivity::class.java).apply {
                putExtra(MUSIC_PARAM, music)
            }
        }
    }
}


