package com.jeanpaulo.buscador_itunes.view.fragment.playlist_list

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragPlaylistBinding
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Playlist
import com.jeanpaulo.buscador_itunes.util.*
import com.jeanpaulo.buscador_itunes.view.FragmentListener
import timber.log.Timber


/**
 * A placeholder fragment containing a simple view.
 */
class PlaylistFragment : Fragment() {

    private val viewModel: PlaylistViewModel by viewModels<PlaylistViewModel> { getViewModelFactory() }
    private lateinit var viewBinding: FragPlaylistBinding

    lateinit var listener: PlaylistFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragPlaylistBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    //MENU FUNCTIONS

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val playlist = listAdapter.getItemSelected()

        when (item.itemId) {
            R.id.context_action_edit -> playlist.playlistId?.let { editPlaylist(it) }
            R.id.context_action_delete -> playlist.playlistId?.let { deletePlaylist(it) }
        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewBinding.refreshLayout, viewBinding.playlistList)
        setupNavigation()
        setupFab()
        initState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as PlaylistFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement PlaylistFragmentListener")
        }
    }

    private fun initState() {
        //inicia a busca
        viewModel.searchPlaylist()

        viewModel.playlistList?.observe(viewLifecycleOwner, Observer { it: List<Playlist> ->
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        })

        viewModel.errorLoading.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                viewBinding.txtError.visibility = View.VISIBLE
                viewBinding.txtError.text = showException(exception)
            } else
                viewBinding.txtError.visibility = View.GONE
        })

        viewBinding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun showException(exception: DataSourceException): String {
        return when (exception.knownNetworkError) {
            DataSourceException.Error.NO_INTERNET_EXCEPTION ->
                getString(R.string.no_internet_connection)
            DataSourceException.Error.TIMEOUT_EXCEPTION ->
                getString(R.string.loading_music_detail_error)

            else -> {
                exception.toString()
            }
        }
    }

    // FAB

    private fun setupFab() {
        listener.setFabDrawableRes(R.drawable.ic_add_white_24dp)
        listener.setFabVisibility(true)
        listener.setFabListener { navigateToAddNewPlaylist() }
    }


    // NAVIGATION

    private fun setupNavigation() {

        viewModel.openPlaylistEvent.observe(viewLifecycleOwner, EventObserver {
            openPlaylist(it)
        })

        viewModel.newPlaylistEvent.observe(viewLifecycleOwner, EventObserver {
            navigateToAddNewPlaylist()
        })
    }

    private fun navigateToAddNewPlaylist() {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment(
                null,
                resources.getString(R.string.add_playlist)
            )
        findNavController().navigate(action)
    }

    private fun openPlaylist(playlistId: String) {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment(
                playlistId,
                resources.getString(R.string.detail_playlist_title)
            )
        findNavController().navigate(action)
    }

    private fun editPlaylist(playlistId: String) {
        val action = PlaylistFragmentDirections
            .actionPlaylistFragmentToAddEditPlaylistFragment(
                playlistId,
                resources.getString(R.string.detail_playlist_title)
            )
        findNavController().navigate(action)
    }

    private fun deletePlaylist(playlistId: String) {
        viewModel.deletePlaylist(playlistId)
    }

    private lateinit var listAdapter: PlaylistListAdapter

    private fun setupListAdapter() {
        val viewModel = viewBinding.viewmodel
        if (viewModel != null) {

            listAdapter =
                PlaylistListAdapter {
                    openPlaylist(it.playlistId!!)
                }
            viewBinding.playlistList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.playlistList.adapter = listAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            viewBinding.playlistList.addItemDecoration(itemDecorator)

        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    // SNACKBAR

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            viewModel.showSnackbarMessage(it.toString())
        }
    }
}

interface PlaylistFragmentListener : FragmentListener {

}