package com.jeanpaulo.buscador_itunes.view.fragment.add_edit_playlist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragAddPlaylistBinding
import com.jeanpaulo.buscador_itunes.util.getViewModelFactory
import com.jeanpaulo.buscador_itunes.util.setupRefreshLayout
import com.jeanpaulo.buscador_itunes.util.setupSnackbar
import com.jeanpaulo.buscador_itunes.view.FragmentListener
import com.jeanpaulo.buscador_itunes.view.fragment.MusicFragmentArgs
import java.lang.ClassCastException

class AddEditPlaylistFragment : Fragment() {

    private lateinit var viewDataBinding: FragAddPlaylistBinding

    private val args: MusicFragmentArgs by navArgs()

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
        return viewDataBinding.root
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
        setupNavigation()
        this.setupRefreshLayout(viewDataBinding.refreshLayout)
        //viewModel.start(args.playlistId)
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
    }

    private fun setupNavigation() {
        /*viewModel.playlistUpdatedEvent.observe(this, EventObserver {
            val action = AddEditPlaylistFragmentDirections.actionAddEditTaskFragmentToTasksFragment(ADD_EDIT_RESULT_OK)
            findNavController().navigate(action)
        })*/
    }
}

interface AddEditPlaylistFragmentListener: FragmentListener {

}
