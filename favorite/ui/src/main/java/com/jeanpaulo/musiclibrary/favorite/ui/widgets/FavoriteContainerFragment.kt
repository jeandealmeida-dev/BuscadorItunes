package com.jeanpaulo.musiclibrary.favorite.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteContainerBinding

class FavoriteContainerFragment(
    val onClickEvent: () -> Unit
) : BaseMvvmFragment() {

    private val viewModel by appViewModel<FavoriteContainerViewModel>()

    private var _binding: FavoriteContainerBinding? = null
    private val binding: FavoriteContainerBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteContainerBinding.inflate(inflater, container, false)
        setupListeners()
        setupEvent()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavoriteCount()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun setupListeners() {
        viewModel.favoriteCountState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteCountState.Success -> {
                    val countString = if(state.total > 1) {
                        resources.getString(R.string.favorite_musics_count)
                    } else {
                        resources.getString(R.string.favorite_music_count)
                    }

                    binding.favoriteDescriptionText.text = countString.format(state.total)
                }

                else -> {}
            }
        }
    }

    fun setupEvent() {
        binding.root.setOnClickListener {
            onClickEvent()
        }
    }
}