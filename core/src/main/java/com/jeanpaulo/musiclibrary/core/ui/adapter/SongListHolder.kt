package com.jeanpaulo.musiclibrary.core.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.databinding.SongItemBinding
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.squareup.picasso.Picasso

class SongListHolder(val binding: SongItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        music: SongUIModel, listener: SongListListener
    ) = with(itemView) {
        binding.musicName.text = music.musicName
        binding.artistName.text = music.artistName ?: "-"
        Picasso.with(binding.root.context).load(music.artworkUrl).into(binding.artwork)

        binding.moreButton.setOnClickListener { listener.onActionPressed(music) }
        setOnClickListener { listener.onPressed(music) }
        setOnLongClickListener {
            listener.onLongPressed(music)
            return@setOnLongClickListener true
        }
    }

    fun onViewRecycled() {
        binding.root.setOnClickListener(null)
        binding.root.setOnLongClickListener(null)
        binding.moreButton.setOnClickListener(null)
    }
}