package com.jeanpaulo.buscador_itunes.playlist.playlist_create

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragAddPlaylistBinding
import com.jeanpaulo.buscador_itunes.music.mvvm.presentation.ADD_EDIT_RESULT_OK
import com.jeanpaulo.buscador_itunes.util.EventObserver
import com.jeanpaulo.buscador_itunes.util.getViewModelFactory
import com.jeanpaulo.buscador_itunes.util.setupRefreshLayout
import com.jeanpaulo.buscador_itunes.util.setupSnackbar
import com.jeanpaulo.buscador_itunes.util.FragmentListener
import java.lang.ClassCastException

class AddEditPlaylistFragment : Fragment() {

    private lateinit var viewDataBinding: FragAddPlaylistBinding

    private val args: AddEditPlaylistFragmentArgs by navArgs()

    private val viewModel by viewModels<AddEditPlaylistViewModel> { getViewModelFactory() }

    lateinit var listener: AddEditPlaylistFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_add_playlist, container, false)
        viewDataBinding = FragAddPlaylistBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        listener.hideKeyboard()
        when (item.itemId) {
            R.id.action_save -> viewModel.savePlaylist()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_playlist_detail, menu)

        val menuSave: MenuItem? = menu.findItem(R.id.action_save)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as AddEditPlaylistFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement AddEditPlaylistFragmentListener")
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupSnackbar()
        setupFab()
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        val playlistId: Long? = args.playlistId?.toLong()
        viewModel.start(playlistId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        viewModel.playlistUpdatedEvent.observe(viewLifecycleOwner, EventObserver {
            val action =
                AddEditPlaylistFragmentDirections.actionAddEditPlaylistFragmentToPlaylistFragment(
                    ADD_EDIT_RESULT_OK
                )
            findNavController().navigate(action)
        })
    }

    private fun setupFab() {
        listener.setFabVisibility(false)
    }
}

interface AddEditPlaylistFragmentListener : FragmentListener {
    fun hideKeyboard()
}
