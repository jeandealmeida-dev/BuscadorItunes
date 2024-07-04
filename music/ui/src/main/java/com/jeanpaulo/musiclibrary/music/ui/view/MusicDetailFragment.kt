package com.jeanpaulo.musiclibrary.music.ui.view

import android.os.Bundle
import android.view.*
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.music.ui.databinding.FragMusicDetailBinding
import com.jeanpaulo.musiclibrary.music.ui.model.MusicDetailUIModel
import com.jeanpaulo.musiclibrary.music.ui.FavoriteState
import com.jeanpaulo.musiclibrary.music.ui.MusicDetailState
import com.jeanpaulo.musiclibrary.music.ui.MusicDetailViewModel
import com.squareup.picasso.Picasso


/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<MusicDetailViewModel>()

    private var _binding: FragMusicDetailBinding? = null
    private val binding: FragMusicDetailBinding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setHasOptionsMenu(true) // use fragment menu
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragMusicDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupWidgets()
        setupMenu()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupListeners() {
        viewModel.musicDetailState.observe(viewLifecycleOwner) { musicDetailState ->
            when (musicDetailState) {
                MusicDetailState.Error -> {
                    binding.layoutProgress.gone()
                    binding.txtError.visible()
                }
                MusicDetailState.Loading -> {
                    binding.txtError.gone()
                    binding.layoutProgress.visible()
                }
                MusicDetailState.Success -> {
                    binding.txtError.gone()
                    binding.layoutProgress.gone()

                    setupMusicDetail(viewModel.musicDetailUIModel)
                }
                else -> {

                }
            }
        }

        viewModel.favoriteState.observe(viewLifecycleOwner) { favoriteState ->
            when (favoriteState) {
                FavoriteState.Error -> {}
                FavoriteState.Loading -> {}
                is FavoriteState.Success -> {
                    activity?.invalidateOptionsMenu()
                }
            }
        }
    }

    fun setupMusicDetail(musicDetailUIModel: MusicDetailUIModel) {
        Picasso.with(context).load(musicDetailUIModel.artwork).into(binding.artcoverImg)
        binding.musicNameTxt.text = musicDetailUIModel.name
        binding.artistNameTxt.text = musicDetailUIModel.artist
        //binding.album.text = musicDetailUIModel.album
    }

    fun setupWidgets() {
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)

        // Text Error
        //binding.txtError.text = getString(R.string.loading_music_detail_error)
        binding.txtError.setOnClickListener {
            //viewModel.refresh()
        }
    }

    fun setupMenu() {

    }

    companion object {
        fun newInstance() =
            MusicDetailFragment().apply {
                val bundle = Bundle()
                arguments = bundle
            }
    }
}