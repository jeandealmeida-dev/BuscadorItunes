package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.showSnackbar
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistCreateViewModel

class PlaylistCreateFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<PlaylistCreateViewModel>()

//    private var _binding: PlaylistCreateFragmentBinding? = null
//    private val binding: PlaylistCreateFragmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = PlaylistCreateFragmentBinding.inflate(inflater, container, false)
//        this.hasOptionsMenu()
        setupListeners()
        setupWidgets()
        return null //_binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //_binding = null
    }

    fun setupListeners(){
        viewModel.playlistCreateState.observe(viewLifecycleOwner) {
            when(it){
                PlaylistCreateState.Error -> showSnackBar("Error")
                PlaylistCreateState.Loading -> TODO()
                PlaylistCreateState.Success -> TODO()
            }
        }
    }

    fun setupWidgets(){
//        setupRefreshLayout(binding.refreshLayout)
//
//        binding.setFabVisibility(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.action_save -> viewModel.savePlaylist()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.menu_playlist_create, menu)
//        val menuSave: MenuItem? = menu.findItem(R.id.action_save)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

//    private fun setupNavigation() {
//        viewModel.playlistUpdatedEvent.observe(viewLifecycleOwner) {
//            val action =
//                AddEditPlaylistFragmentDirections.actionAddEditPlaylistFragmentToPlaylistFragment(
//                    ADD_EDIT_RESULT_OK
//                )
//            findNavController().navigate(action)
//        }
//    }
}
