package com.jeanpaulo.musiclibrary.playlist.ui.fragments

import android.os.Bundle
import android.view.*
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.showSnackbar
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDetailState
import com.jeanpaulo.musiclibrary.playlist.ui.viewmodel.PlaylistDetailViewModel

class PlaylistDetailFragment : BaseMvvmFragment() {

    private val viewModel by appActivityViewModel<PlaylistDetailViewModel>()

    //private var _binding: PlaylistDetailFragmentBinding? = null
    //private val binding: PlaylistDetailFragmentBinding get() = _binding!!

    //private val args: DetailPlaylistFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //_binding = PlaylistDetailFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupListeners()
        setupWidgets()
        return null //_binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //_binding = null
    }

    private fun setupListeners() {
        viewModel.playlistDetailState.observe(viewLifecycleOwner){
            when(it){
                PlaylistDetailState.Error -> showSnackBar("Error")
                PlaylistDetailState.Loading -> TODO()
                PlaylistDetailState.Success -> TODO()
            }
        }
    }

    fun setupWidgets(){
//        setupRefreshLayout(binding.refreshLayout)
//        binding.setFabVisibility(false)
        setupNavigation()

//        val playlistId = args.playlistId?.toLong()
//        viewModel.start(playlistId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //R.id.action_save -> viewModel ()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_playlist_create, menu)
//
//        val menuSave: MenuItem? = menu.findItem(R.id.action_save)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        /*viewModel.playlistUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                AddEditPlaylistFragmentDirections.actionAddEditPlaylistFragmentToPlaylistFragment(
                    ADD_EDIT_RESULT_OK
                )
            findNavController().navigate(action)
        })*/
    }
}