package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteContainerBinding

class FavoriteContainerFragment : BaseMvvmFragment() {

    private var _binding: FavoriteContainerBinding? = null
    private var listener: Listener? = null

    private val viewModel by appViewModel<FavoriteContainerViewModel>()

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
        viewModel.getFavoriteCount()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        viewModel.favoriteCountState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ViewState.Success -> {
                    _binding?.txtDescription?.text =
                        formatMusicCountText(state.data)
                }

                ViewState.Error -> {
                    _binding?.txtDescription?.gone()
                }

                else -> {}
            }
        }
    }

    private fun formatMusicCountText(count: Int) = if (count > 1) {
        resources.getString(R.string.favorite_musics_count)
    } else {
        resources.getString(R.string.favorite_music_count)
    }.format(count)

    private fun setupEvent() {
        _binding?.root?.setOnClickListener {
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