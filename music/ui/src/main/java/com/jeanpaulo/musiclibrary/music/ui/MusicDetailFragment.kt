package com.jeanpaulo.musiclibrary.music.ui

import android.os.Bundle
import android.view.*
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.gone
import com.jeanpaulo.musiclibrary.commons.extensions.visible
import com.jeanpaulo.musiclibrary.music.ui.databinding.FragMusicDetailBinding
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.MusicDetailState
import com.jeanpaulo.musiclibrary.music.ui.viewmodel.MusicDetailViewModel


/**
 * A placeholder fragment containing a simple view.
 */
class MusicDetailFragment : BaseMvvmFragment() {
    val viewModel by appActivityViewModel<MusicDetailViewModel>()

    private var _binding: FragMusicDetailBinding? = null
    private val binding: FragMusicDetailBinding  get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragMusicDetailBinding.inflate(inflater, container, false)
        setupListeners()
        setupWidgets()
        return binding.root
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
                }
            }
        }

        viewModel.musicDetailUIModel.observe(viewLifecycleOwner) { musicDetailUIModel ->
            binding.artist.text = musicDetailUIModel.artist
            binding.album.text = musicDetailUIModel.album
        }
    }

    fun setupWidgets() {
        //setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)

        // Text Error
        //binding.txtError.text = getString(R.string.loading_music_detail_error)
        binding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }

//    private fun setupSnackbar() {
//        view?.setupSnackbar(this, vm.snackbarTextLiveData, Snackbar.LENGTH_SHORT)
//        arguments?.let {
//            vm.snackbarTextLiveData.value = R.string.no_internet_connection
//        }
//    }

    companion object {
        fun newInstance() =
            MusicDetailFragment().apply {
                val bundle = Bundle()
                arguments = bundle
            }
    }
}