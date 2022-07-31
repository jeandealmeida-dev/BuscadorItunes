package com.jeanpaulo.buscador_itunes.favorite.presentation.view

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragFavoriteBinding
import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.util.*
import com.jeanpaulo.buscador_itunes.util.FragmentListener
import com.jeanpaulo.buscador_itunes.favorite.presentation.viewmodel.FavoriteViewModel
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import timber.log.Timber


/**
 * A placeholder fragment containing a simple view.
 */
class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModels<FavoriteViewModel> { getViewModelFactory() }
    private lateinit var viewBinding: FragFavoriteBinding

    lateinit var listener: FavoriteFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragFavoriteBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    //MENU FUNCTIONS

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val music = listAdapter.getItemSelected()

        when (item.itemId) {
            R.id.context_action_remove_fav -> music.id?.let { it -> removeMusicFromFavorite(it) }
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
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.playlistList)
        setupNavigation()
        setupFab()
        initState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as FavoriteFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement FavoriteFragmentListener")
        }
    }

    override fun onResume() {
        super.onResume()
        //inicia a busca
        viewModel.getFavoritePlaylist()
    }

    private fun initState() {
        viewModel.musicList?.observe(viewLifecycleOwner, Observer { it: List<Music> ->
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
        listener.setFabVisibility(false)
    }


    // NAVIGATION

    private fun setupNavigation() {
    }

    private lateinit var listAdapter: MusicInFavoriteAdapter

    private fun setupListAdapter() {
        val viewModel = viewBinding.viewmodel
        if (viewModel != null) {

            listAdapter =
                MusicInFavoriteAdapter { view, music ->
                    openMusicDetail(view, music.id ?: 0L, music.name!!, music.artworkUrl!!)
                }
            viewBinding.musicList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.musicList.adapter = listAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            viewBinding.musicList.addItemDecoration(itemDecorator)

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

    private fun openMusicDetail(view: View, musicId: Long, musicName: String, artworkUrl: String) {
        listener.openMusicDetailActivity(view, musicId, musicName, artworkUrl)
    }

    private fun removeMusicFromFavorite(musicId: Long) {
        viewModel.removeMusicFromFavorite(musicId)
    }
}

interface FavoriteFragmentListener : FragmentListener {
    fun openMusicDetailActivity(view: View, musicId: Long, musicName: String, artworkUrl: String)
}

