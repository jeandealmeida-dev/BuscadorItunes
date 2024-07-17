package com.jeanpaulo.musiclibrary.music.ui.bottom_sheet

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jeanpaulo.musiclibrary.commons.extensions.ui.getDimenAttr
import com.jeanpaulo.musiclibrary.core.domain.model.Song
import com.jeanpaulo.musiclibrary.music.ui.databinding.MiniPlayerBottomSheetBinding
import com.jeanpaulo.musiclibrary.core.music_player.MPService
import com.squareup.picasso.Picasso

class MiniPlayerBottomSheet(
    val binding: MiniPlayerBottomSheetBinding,
    val listener: MiniPlayerBottomSheetListener
) {

    private var context: Context = binding.root.context
    private lateinit var miniPlayerBottomSheet: BottomSheetBehavior<*>

    init {
        setupBehavior()
        setupWidgets()
    }

    private fun setupBehavior() {
        miniPlayerBottomSheet = BottomSheetBehavior.from(binding.root).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            skipCollapsed = false

            addBottomSheetCallback(object :
                BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        MPService.stop(context)
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }
            })
        }
    }

    private fun setupWidgets() {
        binding.root.setOnClickListener {
            listener.onPlayerPressed()
        }

        binding.playButton.let { imageButton ->
            imageButton.setOnClickListener {
                if (!imageButton.isSelected) {
                    MPService.play(context)
                } else {
                    MPService.pause(context)
                }
            }
        }

        // Set peek height
        miniPlayerBottomSheet.peekHeight = context.resources.getDimensionPixelSize(
            context.getDimenAttr(
                android.R.attr.actionBarSize
            )
        )

    }

    fun updatePlayer(song: Song) {
        if (miniPlayerBottomSheet.state == BottomSheetBehavior.STATE_HIDDEN)
            miniPlayerBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

        binding.apply {
            musicName.text = song.name
            artistName.text = song.artist
            Picasso.with(root.context).load(song.artworkUrl).into(artwork)
            playButton.isSelected = true
        }
    }

    fun isPlaying(playing: Boolean = true) {
        binding.playButton.isSelected = playing
    }

    fun expand() {
        miniPlayerBottomSheet.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun hide() {
        miniPlayerBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }


    interface MiniPlayerBottomSheetListener {
        fun onPlayerPressed()
    }


    companion object {
        fun newInstance(
            binding: MiniPlayerBottomSheetBinding,
            listener: MiniPlayerBottomSheetListener
        ): MiniPlayerBottomSheet = MiniPlayerBottomSheet(binding, listener)
    }

}