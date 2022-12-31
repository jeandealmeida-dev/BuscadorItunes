package com.jeanpaulo.musiclibrary.music.ui

import android.graphics.drawable.StateListDrawable
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.gone
import com.jeanpaulo.musiclibrary.commons.extensions.menuChecked
import com.jeanpaulo.musiclibrary.commons.extensions.visible
import com.jeanpaulo.musiclibrary.music.ui.databinding.FragMusicDetailBinding
import com.jeanpaulo.musiclibrary.music.ui.model.MusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.FavoriteState
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.MusicDetailState
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.MusicDetailViewModel


/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<MusicDetailViewModel>()

    private var _binding: FragMusicDetailBinding? = null
    private val binding: FragMusicDetailBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true) // use fragment menu
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragMusicDetailBinding.inflate(inflater, container, false)
        setupListeners()
        setupWidgets()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
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

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
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
            }
        }
    }

    fun setupMusicDetail(musicDetailUIModel: MusicDetailUIModel) {
        binding.artist.text = musicDetailUIModel.artist
        binding.album.text = musicDetailUIModel.album
    }

    fun setupWidgets() {
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)

        // Text Error
        //binding.txtError.text = getString(R.string.loading_music_detail_error)
        binding.txtError.setOnClickListener {
            //viewModel.refresh()
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