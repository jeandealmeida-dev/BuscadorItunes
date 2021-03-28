package com.jeanpaulo.buscador_itunes.view.fragment

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragMusicDetailBinding
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.model.util.NetworkState
import com.jeanpaulo.buscador_itunes.util.*
import com.jeanpaulo.buscador_itunes.view.activity.MusicDetailActivity
import com.jeanpaulo.buscador_itunes.view.adapter.TrackListAdapter
import com.jeanpaulo.buscador_itunes.view_model.MusicDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_music_detail.*
import kotlinx.android.synthetic.main.frag_music_detail.*
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : Fragment() {

    companion object {
        fun newInstance() = MusicDetailFragment()
    }

    private val viewModel: MusicDetailViewModel by viewModels<MusicDetailViewModel> { getViewModelFactory() }
    private lateinit var viewBinding: FragMusicDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragMusicDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)

        //set musicID on viewModel
        val musicId = (activity as MusicDetailActivity).getTrackIdParameter()
        viewModel.setMusicId(musicId)

        return viewBinding.root
    }

    //MENU FUNCTIONS

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
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)
        setupNavigation()
        setupFab()
        initState()
    }

    private fun initState() {
        viewModel.music.observe(viewLifecycleOwner, Observer { it: Music? ->
            if (it != null) {
                viewBinding.music = it
                setupToolbar(it.name)
            }
        })

        viewModel.snackbarText.observe(viewLifecycleOwner, Observer {
            //showSnackbarMessage(it)
        })

        viewModel.errorLoading.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                txt_error.visibility = View.VISIBLE
                txt_error.text = showException(exception)
            } else
                txt_error.visibility = View.GONE
        })

        txt_error.setOnClickListener {
            viewModel.retry()
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

    private fun setupNavigation() {}

    private fun setupFab() {

    }

    private fun setupToolbar(trackName: String?) {
        if(activity is MusicDetailActivity){
            (activity as MusicDetailActivity).setToolbarName(trackName)
        }
    }


    private lateinit var trackListAdapter: TrackListAdapter

    private fun setupListAdapter() {
        val viewModel = viewBinding.viewmodel
        if (viewModel != null) {

            /*trackListAdapter = TrackListAdapter(viewModel) {
                //openMusicDetail(it)
            }
            viewBinding.musicList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.musicList.adapter = trackListAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            viewBinding.musicList.addItemDecoration(itemDecorator);*/

        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    //PROGRESS BAR


    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            viewModel.showSnackbarMessage(R.string.no_internet_connection)
        }
    }


}
