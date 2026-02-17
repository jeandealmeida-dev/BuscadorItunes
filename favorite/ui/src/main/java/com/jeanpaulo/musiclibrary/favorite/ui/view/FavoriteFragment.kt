package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.addDivider
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showTopSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.ds.ui.adapter.SongListAdapter
import com.jeanpaulo.musiclibrary.ds.ui.adapter.SongListListener
import com.jeanpaulo.musiclibrary.ds.ui.adapter.SongListSkeleton
import com.jeanpaulo.musiclibrary.ds.ui.bottomsheet.SongOption
import com.jeanpaulo.musiclibrary.ds.ui.bottomsheet.SongOptionsBottomSheet
import com.jeanpaulo.musiclibrary.ds.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteFragmentBinding

class FavoriteFragment : BaseMvvmFragment() {
    private val viewModel by appViewModel<FavoriteViewModel>()

    private var _binding: FavoriteFragmentBinding? = null
    private var skeleton: SongListSkeleton? = null
    private var listAdapter: SongListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FavoriteFragmentBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar()
        setupListeners()
        setupFab()
        setupAdapter()
        setupSkeleton()

        viewModel.getFavoriteList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Setups

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(_binding?.toolbar)
            it.supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupSkeleton() {
        _binding?.let { skeleton = SongListSkeleton(it.listFavorite) }
    }

    private fun setupFab() {
        _binding?.playAllButton?.setOnClickListener {
            listAdapter?.let {
                viewModel.playSongList(requireContext(), it.getList())
            }
        }
    }

    private fun setupAdapter() {
        listAdapter =
            SongListAdapter(object : SongListListener {

                override fun onPressed(music: SongUIModel) {
                    viewModel.playMusic(requireContext(), music)
                }

                override fun onLongPressed(music: SongUIModel) {
                    viewModel.options(music)
                }

                override fun onActionPressed(music: SongUIModel) {
                    viewModel.options(music)
                }

            })
        _binding?.listFavorite?.apply {
            adapter = listAdapter
            addDivider()
        }
    }

    private fun setupListeners() {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ViewState.Loading -> {
                    _binding?.txtEmpty?.gone()
                    _binding?.txtError?.gone()
                    skeleton?.showSkeletons()
                }

                ViewState.Empty -> {
                    skeleton?.hideSkeletons()
                    _binding?.txtEmpty?.visible()
                    _binding?.listFavorite?.gone()
                }

                ViewState.Error -> {
                    skeleton?.hideSkeletons()
                    _binding?.txtError?.visible()
                    _binding?.listFavorite?.gone()
                }

                is ViewState.Success -> {
                    _binding?.txtError?.gone()
                    _binding?.txtEmpty?.gone()
                    _binding?.listFavorite?.visible()

                    handleSuccess(state.data)
                }
            }
        }

        viewModel.favoriteAction.observe(viewLifecycleOwner) { action ->
            when (action) {
                is FavoriteAction.ShowMusicOptions -> {
                    showMusicOptions(action.music)
                }

                is FavoriteAction.Removed -> {
                    val root = _binding?.root ?: return@observe
                    requireContext().showTopSnackbar(
                        view = root.rootView,
                        text = getString(R.string.favorite_remove_success)
                    )
                }
            }
        }
    }

    // Handle
    private fun handleSuccess(data: List<SongUIModel>) {
        skeleton?.hideSkeletons()
        listAdapter?.submitList(data)
    }

    // Show Music Options

    private fun showMusicOptions(music: SongUIModel) {
        SongOptionsBottomSheet.newInstance(
            music,
            listOf(
                SongOption.REMOVE_FAVORITE,
                SongOption.GO_TO_ARTIST,
            ),
            object : SongOptionsBottomSheet.MusicOptionListener {
                override fun onOptionSelected(searchOption: SongOption, song: SongUIModel) {
                    when (searchOption) {
                        SongOption.REMOVE_FAVORITE -> {
                            viewModel.remove(song)
                        }

                        else -> {}
                    }
                }
            }
        ).show(parentFragmentManager, SongOptionsBottomSheet.TAG)
    }
}

