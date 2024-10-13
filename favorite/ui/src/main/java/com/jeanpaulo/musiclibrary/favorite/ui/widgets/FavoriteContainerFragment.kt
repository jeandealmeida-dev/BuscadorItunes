package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteContainerBinding

class FavoriteContainerFragment : BaseMvvmFragment() {

    private val viewModel by appViewModel<FavoriteContainerViewModel>()

    private var _binding: FavoriteContainerBinding? = null
    private val binding: FavoriteContainerBinding get() = requireNotNull(_binding)

    private var skeleton: FavoriteContainerSkeleton? = null
    private var listener: Listener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FavoriteContainerBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupEvent()
        setupView()

        viewModel.getFavoriteCount()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView() {
        skeleton = FavoriteContainerSkeleton(binding.txtDescription)
    }

    private fun setupListeners() {
        viewModel.favoriteCountState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Success -> {
                    skeleton?.hideSkeletons()
                    binding.txtDescription.text =
                        formatMusicCountText(state.data).format(state.data)
                }

                ViewState.Loading -> {
                    skeleton?.showSkeletons()
                }

                ViewState.Error -> {
                    skeleton?.hideSkeletons()
                }

                else -> {}
            }
        }
    }

    private fun formatMusicCountText(count: Int) = if (count > 1) {
        resources.getString(R.string.favorite_musics_count)
    } else {
        resources.getString(R.string.favorite_music_count)
    }

    private fun setupEvent() {
        binding.root.setOnClickListener {
            listener?.onClickEvent()
        }
    }

    interface Listener {
        fun onClickEvent()
    }

    companion object {

        fun newInstance(listener: Listener) =
            FavoriteContainerFragment().apply {
                this.listener = listener
            }
    }
}