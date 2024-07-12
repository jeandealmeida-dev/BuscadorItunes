package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.favorite.ui.widgets.FavoriteContainerFragment
import com.jeanpaulo.musiclibrary.playlist.ui.PlaylistListAdapter
import com.jeanpaulo.musiclibrary.playlist.ui.R
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.PlaylistFragmentBinding
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDeleteState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistListState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistViewModel

class PlaylistFragment : BaseMvvmFragment() {
    val viewModel by appViewModel<PlaylistViewModel>()

    private var _binding: PlaylistFragmentBinding? = null
    private val binding: PlaylistFragmentBinding get() = _binding!!

    private lateinit var listAdapter: PlaylistListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = PlaylistFragmentBinding
        .inflate(inflater, container, false).also {
            _binding = it
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupWidgets()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        viewModel.playlistListState.observe(viewLifecycleOwner) { state ->
            //reset
            binding.errorPlaylistLayout.gone()

            when (state) {
                PlaylistListState.Loading -> {}

                PlaylistListState.Error -> {
                    binding.errorPlaylistLayout.apply {
                        visible()
                        setOnClickListener {
                            viewModel.refresh()
                        }
                    }
                }

                is PlaylistListState.Success -> {
                    listAdapter.submitList(state.playlistList)
                }

                else -> {}
            }
        }

        viewModel.playlistDeleteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                PlaylistDeleteState.Error -> showSnackBar(getString(R.string.playlist_delete_error))
                PlaylistDeleteState.Loading -> showSnackBar(getString(R.string.playlist_delete_loading))
                PlaylistDeleteState.Success -> showSnackBar(getString(R.string.playlist_delete_success))
            }
        }
    }

    private fun setupWidgets() {
        setupFavoriteContainer()
        setupListAdapter()
        setupRefreshLayout(binding.refreshLayout, binding.playlistList)

        binding.fab.setOnClickListener {
            navigateToPlaylistCreate()
        }
    }

    fun setupFavoriteContainer() {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.favorite_container, FavoriteContainerFragment {
                navigateToFavorites()
            })
            .commit()
    }

    private fun setupListAdapter() {
        listAdapter =
            PlaylistListAdapter(object : PlaylistListAdapter.Listener {
                override fun onPlaylistSelected(playlist: Playlist) {
                    navigateToPlaylistDetail()
                }

                override fun onFavoriteSelected() {
                    navigateToPlaylistDetail()
                }

            })
        binding.playlistList.layoutManager =
            CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.playlistList.adapter = listAdapter

        /*val itemDecorator =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

        val itemDecorator = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )

        binding.playlistList.addItemDecoration(itemDecorator)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val playlist = listAdapter.getItemSelected()

        when (item.itemId) {
            //R.id.context_action_edit -> {} editPlaylist(playlist.playlistId)
            R.id.context_action_delete -> deletePlaylist(playlist.playlistId)
        }
        return super.onContextItemSelected(item)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    // NAVIGATION

    private fun navigateToFavorites() {
        findNavController().navigate(
            PlaylistFragmentDirections
                .actionPlaylistFragmentToFavoriteFragment()
        )
    }

    private fun navigateToPlaylistCreate() {
        findNavController().navigate(
            PlaylistFragmentDirections
                .actionPlaylistFragmentToPlaylistCreateFragment()
        )
    }

    private fun navigateToPlaylistDetail() {
        findNavController().navigate(
            PlaylistFragmentDirections
                .actionPlaylistFragmentToPlaylistDetailFragment()
        )
    }

    private fun deletePlaylist(playlistId: Long) {
        viewModel.deletePlaylist(playlistId)
    }
}