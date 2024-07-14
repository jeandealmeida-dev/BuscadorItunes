package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showSnackbar
import com.jeanpaulo.musiclibrary.playlist.ui.R
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.PlaylistDetailFragmentBinding
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDetailState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDetailViewModel

class PlaylistDetailFragment : BaseMvvmFragment() {

    private val viewModel by appViewModel<PlaylistDetailViewModel>()

    private var _binding: PlaylistDetailFragmentBinding? = null
    private val binding: PlaylistDetailFragmentBinding get() = _binding!!

    //private val args: DetailPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistDetailFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupListeners()
        setupWidgets()
        setupMenu()
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        viewModel.playlistDetailState.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistDetailState.Error -> showSnackBar("Error")
                PlaylistDetailState.Loading -> TODO()
                is PlaylistDetailState.Success -> TODO()
                else -> {}
            }
        }
    }

    fun setupWidgets() {
        setupRefreshLayout(binding.refreshLayout)
        //binding.setFabVisibility(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        //TODO Jean fix menu
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_playlist_create, menu)

        val menuSave: MenuItem? = menu.findItem(R.id.action_save)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun navigateToDetailFragment() {
        val action =
            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistDetailFragment()
        findNavController().navigate(action)
    }

    fun setupMenu() {
        requireActivity()
            .addMenuProvider(
                PlaylistDetailFragmentMenuProvider(),
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
    }

    inner class PlaylistDetailFragmentMenuProvider : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_playlist, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                android.R.id.home -> {
                    requireActivity().onBackPressed()
                    true
                }

                else -> {
                    false
                }
            }
        }
    }
}