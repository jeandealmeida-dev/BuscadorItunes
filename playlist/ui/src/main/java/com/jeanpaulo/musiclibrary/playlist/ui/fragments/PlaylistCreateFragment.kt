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
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.PlaylistCreateFragmentBinding
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateViewModel

class PlaylistCreateFragment : BaseMvvmFragment() {
    val viewModel: PlaylistCreateViewModel by appViewModel<PlaylistCreateViewModel>()

    private var _binding: PlaylistCreateFragmentBinding? = null
    private val binding: PlaylistCreateFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistCreateFragmentBinding.inflate(inflater, container, false)
        return _binding?.root
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

    fun setupListeners() {
        viewModel.playlistCreateState.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistCreateState.Error -> showSnackBar("Error")
                PlaylistCreateState.Loading -> showSnackBar("Loading")
                is PlaylistCreateState.Success -> {
                    navigateBack()
                    showSnackBar("Success")
                }
                else -> {}
            }
        }
    }

    fun setupWidgets() {
        setupRefreshLayout(binding.refreshLayout)
    }

    private fun setupMenu() {
        requireActivity()
            .addMenuProvider(
                PlaylistCreateFragmentMenuProvider(),
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun navigateToCreate() {
        findNavController().navigate(
            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistCreateFragment()
        )
    }

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    inner class PlaylistCreateFragmentMenuProvider : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_playlist_create, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                android.R.id.home -> {
                    navigateBack()
                    true
                }
                R.id.action_save -> {
                    viewModel.createPlaylist(
                        title = binding.inputPlaylistTitle.text.toString(),
                        description = binding.inputPlaylistDescription.text.toString()
                    )
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}
