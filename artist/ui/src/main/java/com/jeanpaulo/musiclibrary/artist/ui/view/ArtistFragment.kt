package com.jeanpaulo.musiclibrary.artist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.artist.ui.databinding.ArtistFragmentBinding
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.domain.model.Artist
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListAdapter
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListListener
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListSkeleton
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.player.mp.MPService

class ArtistFragment : BaseMvvmFragment() {
    private val viewModel by appViewModel<ArtistViewModel>()

    private var _binding: ArtistFragmentBinding? = null
    private val binding: ArtistFragmentBinding get() = requireNotNull(_binding)

    private var skeleton: SongListSkeleton? = null
    private var listAdapter: SongListAdapter? = null

    private val args: ArtistFragmentArgs by navArgs()
    private val artistId: Long
        get() = args.artistId

    private val artistName: String?
        get() = args.artistName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ArtistFragmentBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar()
        setupListeners()
        setupAdapter()
        setupSkeleton()

        viewModel.getArtist(artistId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Setups

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
            }
            it.title = artistName
        }
    }

    private fun setupSkeleton() {
        skeleton = SongListSkeleton(binding.rcvPopularMusics)
    }


    private fun setupListeners() {
        viewModel.artistState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ViewState.Error -> handleError()
                ViewState.Loading -> handleLoading()
                is ViewState.Success -> {
                    if (state.data.popularMusic.isEmpty()) {
                        handleError()
                    } else {
                        handleSuccess(state.data)
                    }
                }

                else -> {}
            }
        }
    }

    private fun setupAdapter() {
        listAdapter =
            SongListAdapter(object : SongListListener {

                override fun onPressed(music: SongUIModel) {
                    val mpSong = music.convertToSong().toMPSong()
                    MPService.playSongList(requireContext(), listOf(mpSong))
                }

                override fun onLongPressed(music: SongUIModel) {
                    // TODO
                }

                override fun onActionPressed(music: SongUIModel) {
                    // TODO
                }

            })
        binding.rcvPopularMusics.layoutManager =
            CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rcvPopularMusics.adapter = listAdapter

        /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

        val itemDecorator = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )

        binding.rcvPopularMusics.addItemDecoration(itemDecorator)
    }

    // Handle

    private fun handleLoading() {
        binding.txtError.gone()

        skeleton?.showSkeletons()
    }

    private fun handleError() {
        skeleton?.hideSkeletons()
        binding.txtError.visible()
    }

    private fun handleSuccess(data: Artist) {
        skeleton?.hideSkeletons()
        setupAdapter()
        listAdapter?.submitList(data.popularMusic.map { SongUIModel.fromModel(it) })
    }
}

