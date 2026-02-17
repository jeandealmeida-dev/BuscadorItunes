package com.jeanpaulo.musiclibrary.ds.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.ds.databinding.SongItemBinding
import com.jeanpaulo.musiclibrary.ds.ui.model.SongUIModel
import com.squareup.picasso.Picasso

class SongListHolder(val binding: SongItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(
        music: SongUIModel, listener: SongListListener
    ) = with(itemView) {
        binding.musicName.text = music.musicName
        binding.artistName.text = music.artistName
        music.artworkUrl?.let { url ->
            Picasso.get().load(url).into(binding.artwork)
        }

        binding.actionButton.setOnClickListener { listener.onActionPressed(music) }
        setOnClickListener { listener.onPressed(music) }
        setOnLongClickListener {
            listener.onLongPressed(music)
            return@setOnLongClickListener true
        }
    }

    fun onViewRecycled() {
        binding.root.setOnClickListener(null)
        binding.root.setOnLongClickListener(null)
        binding.actionButton.setOnClickListener(null)
    }
}