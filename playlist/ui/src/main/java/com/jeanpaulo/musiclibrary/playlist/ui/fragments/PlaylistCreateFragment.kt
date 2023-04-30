package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.showSnackbar
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
                PlaylistCreateState.Loading -> TODO()
                is PlaylistCreateState.Success -> TODO()
                else -> {}
            }
        }
    }

    fun setupWidgets() {
        setupRefreshLayout(binding.refreshLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> viewModel.createPlaylist(
                title = binding.inputPlaylistTitle.text.toString(),
                description = binding.inputPlaylistDescription.text.toString()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_playlist_create, menu)
        val menuSave: MenuItem? = menu.findItem(R.id.action_save)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupMenu() {
        //TODO Jean fix menu
        //hasOptionsMenu()
        //requireActivity()
        //    .addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun navigateToCreate() {
        findNavController().navigate(
            PlaylistFragmentDirections.actionPlaylistFragmentToPlaylistCreateFragment()
        )
    }
}
