package com.jeanpaulo.musiclibrary.music.ui.v2.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.ViewCompat
import androidx.lifecycle.Lifecycle
import androidx.palette.graphics.Palette
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.gone
import com.jeanpaulo.musiclibrary.commons.extensions.menuChecked
import com.jeanpaulo.musiclibrary.commons.extensions.visible
import com.jeanpaulo.musiclibrary.music.ui.R
import com.jeanpaulo.musiclibrary.music.ui.databinding.FragMusicDetailV2Binding
import com.jeanpaulo.musiclibrary.music.ui.model.MusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.v2.FavoriteState
import com.jeanpaulo.musiclibrary.music.ui.v2.MusicDetailState
import com.jeanpaulo.musiclibrary.music.ui.v2.MusicDetailViewModel
import com.squareup.picasso.Picasso


/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<MusicDetailViewModel>()

    private var _binding: FragMusicDetailV2Binding? = null
    private val binding: FragMusicDetailV2Binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragMusicDetailV2Binding.inflate(inflater, container, false)
        setupListeners()
        setupWidgets()
        setupAnimations()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(
            MusicDetailFragmentMenu(),
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupListeners() {
        viewModel.musicDetailState.observe(viewLifecycleOwner) { musicDetailState ->
            when (musicDetailState) {
                MusicDetailState.Error -> {
                    binding.layoutProgress.gone()
                    binding.txtError.visible()
                }
                MusicDetailState.Loading -> {
                    binding.txtError.gone()
                    binding.layoutProgress.visible()
                }
                MusicDetailState.Success -> {
                    binding.txtError.gone()
                    binding.layoutProgress.gone()

                    setupMusicDetail(viewModel.musicDetailUIModel)
                }
                else -> {

                }
            }
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) { favoriteState ->
            when (favoriteState) {
                FavoriteState.Error -> {}
                FavoriteState.Loading -> {}
                is FavoriteState.Success -> {
                    activity?.invalidateOptionsMenu()
                }
                else -> {}
            }
        }
    }

    fun setupMusicDetail(musicDetailUIModel: MusicDetailUIModel) {
        binding.trackName.text = musicDetailUIModel.name
        binding.collectionName.text = musicDetailUIModel.album
        binding.artistName.text = musicDetailUIModel.artist

        Picasso.with(binding.root.context).load(musicDetailUIModel.artwork)
            .into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    binding.artwork.setImageBitmap(bitmap)
                    if (bitmap != null)
                        setBackgroundColor(bitmap)
                }

                override fun onBitmapFailed(errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }

            })
    }

    fun setBackgroundColor(bitmap: Bitmap) {
        Palette.from(bitmap).generate { palette ->
            val mutedColor = palette!!.getLightVibrantColor(androidx.appcompat.R.attr.colorPrimary)
            binding.layout.setBackgroundColor(mutedColor)
        }
    }

    fun setupWidgets() {
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)

        // Text Error
        //binding.txtError.text = getString(R.string.loading_music_detail_error)
        binding.txtError.setOnClickListener {
            //viewModel.refresh()
        }
    }

    //ANIMATIONS
    fun setupAnimations() {
        ViewCompat.setTransitionName(
            binding.artwork,
            MusicDetailActivity.VIEW_NAME_HEADER_IMAGE
        );
        ViewCompat.setTransitionName(
            binding.trackName,
            MusicDetailActivity.VIEW_NAME_HEADER_TITLE
        );
    }

    inner class MusicDetailFragmentMenu : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_music_detail, menu)
            menu.findItem(R.id.action_favorite).menuChecked(viewModel.isFavorite)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                android.R.id.home -> {
                    requireActivity().onBackPressed()
                    true
                }
                R.id.action_favorite -> {
                    viewModel.clickFavoriteMenu()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    companion object {
        fun newInstance() =
            MusicDetailFragment().apply {
                val bundle = Bundle()
                arguments = bundle
            }
    }
}