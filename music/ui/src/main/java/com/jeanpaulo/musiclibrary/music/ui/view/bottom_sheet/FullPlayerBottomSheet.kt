package com.jeanpaulo.musiclibrary.music.ui.view.bottom_sheet

import android.content.Context
import android.view.View
import android.view.WindowManager
import androidx.core.content.getSystemService
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jeanpaulo.musiclibrary.commons.extensions.ui.getDimenAttr
import com.jeanpaulo.musiclibrary.commons.extensions.ui.setFullScreen
import com.jeanpaulo.musiclibrary.core.domain.model.MusicPlayerSong
import com.jeanpaulo.musiclibrary.core.service.MusicPlayerService
import com.jeanpaulo.musiclibrary.music.ui.databinding.FullPlayerBottomSheetBinding
import com.squareup.picasso.Picasso


class FullPlayerBottomSheet(
    val binding: FullPlayerBottomSheetBinding,
    val listener: FullPlayerBottomSheetListener
) {

    private var context: Context = binding.root.context
    private lateinit var fullPlayerBottomSheet: BottomSheetBehavior<*>

    init {
        setupBehavior()
        setupWidgets()
    }

    private fun setupBehavior() {
        fullPlayerBottomSheet = BottomSheetBehavior.from(binding.root).apply {
            // Expanded by default
            state = BottomSheetBehavior.STATE_HIDDEN
            skipCollapsed = false //Avoid collapsed state
            isHideable = true
            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {

                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun setupWidgets() {
        binding.toolbar.setNavigationOnClickListener {
            hide()
        }

        binding.playButton.let { imageButton ->
            imageButton.setOnClickListener {
                if (!imageButton.isSelected) {
                    MusicPlayerService.play(context)
                } else {
                    MusicPlayerService.pause(context)
                }
            }
        }

        // Set peek height
        fullPlayerBottomSheet.peekHeight = context.resources.getDimensionPixelSize(
            context.getDimenAttr(
                android.R.attr.actionBarSize
            )
        )


//        binding.root.setBackgroundResource(com.jeanpaulo.musiclibrary.commons.R.drawable.bottom_sheet_background)
        context.getSystemService<WindowManager>()?.let {
            binding.root.setFullScreen(it)
        }
    }

    fun expand() {
        fullPlayerBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        fullPlayerBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }

    fun isPlaying(playing: Boolean = true) {
        binding.playButton.isSelected = playing
    }

    fun updatePlayer(song: MusicPlayerSong) {
        binding.apply {
            musicNameTxt.text = song.name
            artistNameTxt.text = song.artist
            Picasso.with(root.context).load(song.artworkUrl).into(artcoverImg)
            playButton.isSelected = true
        }
    }

    interface FullPlayerBottomSheetListener {
        fun onDismiss()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        fun newInstance(
            binding: FullPlayerBottomSheetBinding,
            listener: FullPlayerBottomSheetListener
        ): FullPlayerBottomSheet {
            return FullPlayerBottomSheet(binding = binding, listener = listener)
        }
    }
}