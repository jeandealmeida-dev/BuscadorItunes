package com.jeanpaulo.musiclibrary.music.ui.v1.view

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.palette.graphics.Palette
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.BaseOnOffsetChangedListener
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmActivity
import com.jeanpaulo.musiclibrary.commons.extensions.gone
import com.jeanpaulo.musiclibrary.music.ui.v1.MusicDetailViewModel
import com.jeanpaulo.musiclibrary.music.ui.v1.MusicPlayerState
import com.jeanpaulo.musiclibrary.commons.extras.MyMediaPlayer
import com.jeanpaulo.musiclibrary.commons.extensions.replace
import com.jeanpaulo.musiclibrary.commons.extensions.visible
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.R
import com.jeanpaulo.musiclibrary.music.ui.databinding.ActivityMusicDetailBinding
import com.squareup.picasso.Picasso


class MusicDetailActivity : BaseMvvmActivity() {

    private val vm by appViewModel<MusicDetailViewModel>()

    private var _binding: ActivityMusicDetailBinding? = null
    private val binding get() = _binding!!

    private var myMediaPlayer: MyMediaPlayer? = null

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

    fun setupListeners() {
        vm.musicPlayerState.observe(this) { playerState ->
            when (playerState) {
                is MusicPlayerState.Setup -> {
                    myMediaPlayer = MyMediaPlayer(this, playerState.uri)
                    binding.fabPreview.visible()
                }
                MusicPlayerState.Stop -> {
                    myMediaPlayer?.stop()
                    binding.fabPreview.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            android.R.drawable.ic_media_play
                        )
                    )
                }

                MusicPlayerState.Play -> {
                    myMediaPlayer?.play()
                    binding.fabPreview.setImageDrawable(
                        ContextCompat.getDrawable(
                            this,
                            android.R.drawable.ic_media_pause
                        )
                    )
                }
            }
        }
    }

    fun setupWidgets() {
        setupCollapsingToolbar()
        setupFAB()
    }

    //COLLAPSION TOOLBAR
    var appBarExpanded = true
    private fun setupCollapsingToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.appBar.addOnOffsetChangedListener(object :
            BaseOnOffsetChangedListener<AppBarLayout>,
            AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                //  Vertical offset == 0 indicates appBar is fully  expanded.
                if (Math.abs(verticalOffset) > 300) {
                    appBarExpanded = false
                    //invalidateOptionsMenu()
                } else {
                    appBarExpanded = true
                    //invalidateOptionsMenu()
                }
            }

        })

        Picasso.with(binding.root.context).load(vm.simpleMusicDetail.artworkUrl)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.imgCollection.setImageBitmap(bitmap)
                    if (bitmap != null)
                        setToolbarColor(bitmap)
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

            })

        binding.collapseToolbar.title = vm.simpleMusicDetail.name
    }

    private fun setToolbarColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val mutedColor = palette!!.getMutedColor(androidx.appcompat.R.attr.colorPrimary)
            binding.collapseToolbar.setContentScrimColor(mutedColor)
        }
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

    override fun onBackPressed() {
        binding.fabPreview.gone()
        binding.content.gone()
        super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        binding.fabPreview.gone()
        myMediaPlayer?.stop()
        _binding = null
    }

    fun setupMusicDetailContainer() {
        supportFragmentManager.replace(
            binding.fragment.id,
            MusicDetailFragment.newInstance(),
            tag = MusicDetailFragment::class.simpleName
        )
    }

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

    fun setupFAB() {
        binding.fabPreview.setOnClickListener {
            vm.changePlayerState()
        }
    }

    companion object {

        const val VIEW_NAME_HEADER_IMAGE = "detail:header:image"
        const val VIEW_NAME_HEADER_TITLE = "detail:header:title"

        const val MUSIC_PARAM = "music_param"
        const val FROM_REMOTE_PARAM = "from_remote_param"

        fun newInstance(context: Context, music: SimpleMusicDetailUIModel, fromRemote: Boolean): Intent {
            return Intent(context, MusicDetailActivity::class.java).apply {
                putExtra(MUSIC_PARAM, music)
                putExtra(FROM_REMOTE_PARAM, fromRemote)
            }
        }
    }
}


