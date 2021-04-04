package com.jeanpaulo.buscador_itunes.view.playlist.detail

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
import com.jeanpaulo.buscador_itunes.databinding.FragPlaylistDetailBinding
import com.jeanpaulo.buscador_itunes.util.EventObserver
import com.jeanpaulo.buscador_itunes.util.getViewModelFactory
import com.jeanpaulo.buscador_itunes.util.setupRefreshLayout
import com.jeanpaulo.buscador_itunes.util.setupSnackbar
import com.jeanpaulo.buscador_itunes.view.FragmentListener
import com.jeanpaulo.buscador_itunes.view.activity.ADD_EDIT_RESULT_OK
import com.jeanpaulo.buscador_itunes.view.fragment.MusicFragmentArgs
import java.lang.ClassCastException

class DetailPlaylistFragment : Fragment() {

    private lateinit var viewDataBinding: FragPlaylistDetailBinding

    private val args: DetailPlaylistFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailPlaylistViewModel> { getViewModelFactory() }

    lateinit var listener: DetailPlaylistFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.frag_add_playlist, container, false)
        viewDataBinding = FragPlaylistDetailBinding.bind(root).apply {
            this.viewmodel = viewModel
        }
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setHasOptionsMenu(true)
        return viewDataBinding.root
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> viewModel.editPlaylist()
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
            listener = context as DetailPlaylistFragmentListener
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
        viewModel.start(args.playlistId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
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

    private fun setupFab() {
        listener.setFabVisibility(false)
    }
}

interface DetailPlaylistFragmentListener : FragmentListener {

}
