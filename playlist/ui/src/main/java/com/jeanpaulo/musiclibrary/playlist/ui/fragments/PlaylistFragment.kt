package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.*
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
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
    ): View? {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupWidgets()
        setupMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        viewModel.playlistListState.observe(viewLifecycleOwner) { state ->
            //reset
            binding.layoutNoPlaylist.gone()
            binding.txtPlaylistError.gone()
            binding.txtPlaylistLoading.gone()

            when (state) {
                PlaylistListState.Error -> {
                    binding.txtPlaylistError.apply {
                        visible()
                        setOnClickListener {
                            viewModel.refresh()
                        }
                    }
                }

                PlaylistListState.Loading -> {
                    binding.txtPlaylistLoading.visible()
                }

                PlaylistListState.Empty -> {
                    binding.layoutNoPlaylist.visible()
                }

                is PlaylistListState.Success -> {
                    if (state.playlistList.isEmpty()) {
                        binding.layoutNoPlaylist.visible()
                    } else {
                        listAdapter.submitList(state.playlistList)
                    }
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
        setupListAdapter()
        setupRefreshLayout(binding.refreshLayout, binding.playlistList)
    }

    private fun setupListAdapter() {
        listAdapter =
            PlaylistListAdapter {
                //openPlaylist(it.playlistId)
            }
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

    //MENU FUNCTIONS

    fun setupMenu() {
        requireActivity()
            .addMenuProvider(
                PlaylistFragmentMenuProvider(),
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val playlist = listAdapter.getItemSelected()

        when (item.itemId) {
            R.id.context_action_edit -> {} //editPlaylist(playlist.playlistId)
            R.id.context_action_delete -> deletePlaylist(playlist.playlistId)
        }
        return super.onContextItemSelected(item)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    // NAVIGATION

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

    inner class PlaylistFragmentMenuProvider : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_playlist, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                android.R.id.home -> {
                    requireActivity().onBackPressed()
                    true
                }

                R.id.action_new -> {
                    navigateToPlaylistCreate()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}