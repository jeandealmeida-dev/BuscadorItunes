package com.jeanpaulo.musiclibrary.core.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jeanpaulo.musiclibrary.commons.R
import com.jeanpaulo.musiclibrary.commons.extensions.addDivider
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.core.databinding.OptionsBottomSheetBinding
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.squareup.picasso.Picasso

class SongOptionsBottomSheet(
    val song: SongUIModel,
    val options: List<SongOption>,
    val listener: MusicOptionListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: OptionsBottomSheetBinding
    private var bottomSheetPeekHeight = 0

    override fun getTheme(): Int = com.jeanpaulo.musiclibrary.core.R.style.AppTheme_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = OptionsBottomSheetBinding.inflate(layoutInflater, container, false).also {
        binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.bottomSheet.setBackgroundResource(R.drawable.bottom_sheet_background)
        // 86dp
        bottomSheetPeekHeight = resources
            .getDimensionPixelSize(com.jeanpaulo.musiclibrary.core.R.dimen.bottom_sheet_default_peek_height)
        setupWidgets()
        setupBehavior()
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private fun setupBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setupWidgets() {
        binding.content.itemMusic.apply {
            musicName.text = song.musicName
            artistName.text = song.artistName
            Picasso.with(requireContext()).load(song.artworkUrl).into(artwork)

            actionButton.gone()
        }

        binding.content.favoriteButton.setOnClickListener {
            listener.onOptionSelected(SongOption.ADD_FAVORITE)
        }

        binding.content.options.adapter = SongOptionsAdapter(options) {
            listener.onOptionSelected(it)
            dismiss()
        }
        binding.content.options.addDivider()
    }

    interface MusicOptionListener {

        fun onOptionSelected(searchOption: SongOption)
    }

    companion object {

        const val TAG = "MusicOptionsBottomSheet"

        fun newInstance(
            song: SongUIModel,
            options: List<SongOption>,
            listener: MusicOptionListener
        ): SongOptionsBottomSheet {
            return SongOptionsBottomSheet(
                song, options, listener
            )
        }
    }
}