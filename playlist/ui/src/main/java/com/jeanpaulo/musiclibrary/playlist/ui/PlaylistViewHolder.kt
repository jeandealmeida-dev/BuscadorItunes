package com.jeanpaulo.musiclibrary.playlist.ui

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.PlaylistItemBinding

class PlaylistViewHolder(val binding: PlaylistItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    constructor(layoutInflater: LayoutInflater, parent: ViewGroup) : this(
        PlaylistItemBinding.inflate(
            layoutInflater,
            parent,
            false
        )
    )

    fun bind(playlist: Playlist, listener: (Playlist) -> Unit) = with(itemView) {
        binding.playlistNameText.text = playlist.title
        binding.playlistDescriptionText.text = playlist.description

        setOnClickListener { listener(playlist) }
        setOnCreateContextMenuListener { menu, v, menuInfo ->
            menu.setHeaderTitle(playlist.title)
            menu.add(
                Menu.NONE, R.id.context_action_edit,
                Menu.NONE, R.string.action_edit
            );
            menu.add(
                Menu.NONE, R.id.context_action_delete,
                Menu.NONE, R.string.action_delete
            );
        }
    }

    fun onViewRecycled() {
        itemView.setOnCreateContextMenuListener(null)
        itemView.setOnClickListener(null)
    }
}