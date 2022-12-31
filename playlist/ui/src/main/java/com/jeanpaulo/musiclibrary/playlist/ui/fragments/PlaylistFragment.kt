package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.showSnackbar
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.playlist.ui.PlaylistListAdapter
import com.jeanpaulo.musiclibrary.playlist.ui.R
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.PlaylistFragmentBinding
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistViewModel

class PlaylistFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<PlaylistViewModel>()

    private var _binding: PlaylistFragmentBinding? = null
    private val binding: PlaylistFragmentBinding get() = _binding!!

    private lateinit var listAdapter: PlaylistListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = PlaylistFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupListeners()
        setupWidgets()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun setupListeners() {
        viewModel.playlistState.observe(viewLifecycleOwner) {
            when (it) {
                PlaylistState.Error -> showSnackBar("Error")
                PlaylistState.Loading -> showSnackBar("Loading")
                PlaylistState.Success -> showSnackBar("Success")
                else -> {}
            }
        }

        viewModel.playlistList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }
    }

    private fun setupWidgets() {
        setupListAdapter()
        setupRefreshLayout(binding.refreshLayout, binding.playlistList)

//        binding.setFabDrawableRes(R.drawable.ic_add_white_24dp)
//        binding.setFabVisibility(true)
//        binding.setFabListener { navigateToAddNewPlaylist() }

        binding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupListAdapter() {
        listAdapter =
            PlaylistListAdapter {
                openPlaylist(it.playlistId)
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

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val playlist = listAdapter.getItemSelected()

        when (item.itemId) {
            R.id.context_action_edit -> editPlaylist(playlist.playlistId)
            R.id.context_action_delete -> deletePlaylist(playlist.playlistId)
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        else -> super.onOptionsItemSelected(item)
    }
    
    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    // NAVIGATION

    private fun navigateToAddNewPlaylist() {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment()
        findNavController().navigate(action)
    }

    private fun openPlaylist(playlistId: Long) {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment()
        findNavController().navigate(action)
    }

    private fun editPlaylist(playlistId: Long) {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment()
        findNavController().navigate(action)
    }

    private fun deletePlaylist(playlistId: Long) {
        viewModel.deletePlaylist(playlistId)
    }
}