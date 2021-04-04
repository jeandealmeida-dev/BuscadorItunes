package com.jeanpaulo.buscador_itunes.view.music.music_detail

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragMusicDetailBinding
import com.jeanpaulo.buscador_itunes.datasource.remote.util.DataSourceException
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.util.MyMediaPlayer
import com.jeanpaulo.buscador_itunes.util.getViewModelFactory
import com.jeanpaulo.buscador_itunes.util.setupSnackbar
import com.jeanpaulo.buscador_itunes.view.FragmentListener
import com.jeanpaulo.buscador_itunes.view.adapter.TrackListAdapter
import com.jeanpaulo.buscador_itunes.view.music.music_search.music_detail.MusicDetailViewModel
import kotlinx.android.synthetic.main.frag_music_detail.*
import timber.log.Timber
import java.lang.ClassCastException


/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : Fragment() {

    private val viewModel: MusicDetailViewModel by viewModels<MusicDetailViewModel> { getViewModelFactory() }
    private lateinit var viewBinding: FragMusicDetailBinding

    lateinit var listener: MusicDetailFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragMusicDetailBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
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
        initState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as MusicDetailFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context} must implement MusicDetailFragmentListener")
        }
    }

    fun getMusicId() {
        //set musicID on viewModel
        val musicId = listener.getTrackIdParameter()
        viewModel.setMusicId(musicId)
    }


    private fun initState() {
        getMusicId()

        viewModel.music.observe(viewLifecycleOwner, Observer { it: Music? ->
            if (it != null) {
                viewBinding.music = it

                //Load Widgets whit model
                setupFab(it.isStreamable, it.previewUrl)
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

    private fun setupNavigation() {}

    lateinit var player: MyMediaPlayer

    var playing = false
    private fun setupFab(isStreamble: Boolean?, previewUri: String?) {

        if (isStreamble != null && isStreamble && previewUri != null) {

            player = MyMediaPlayer(previewUri) {
                playing = it
                val imageResource =
                    if (playing) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play

                listener.setFabDrawableRes(imageResource)
            }
            player.create(context)

            listener.setFabVisibility(true)
            listener.setFabListener {
                if (!playing) player.play() else player.pause()
            }
        } else {
            listener.setFabVisibility(true)
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


    override fun onStop() {
        super.onStop()
        listener.setFabVisibility(false)
        player.release()
    }

}

interface MusicDetailFragmentListener : FragmentListener {
    fun getTrackIdParameter(): Long
}
