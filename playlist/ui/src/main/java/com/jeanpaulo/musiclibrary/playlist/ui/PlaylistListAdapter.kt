package com.jeanpaulo.musiclibrary.playlist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist

class PlaylistListAdapter(
    private val listener: Listener
) : RecyclerView.Adapter<PlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder =
        PlaylistViewHolder(
            LayoutInflater.from(parent.context), parent
        )
    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {

        val playlist = asyncListDiffer.currentList[position]
        holder.bind(playlist) {
            listener.onPlaylistSelected(it)
        }
    }

    override fun onViewRecycled(holder: PlaylistViewHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    fun submitList(newList: List<Playlist>) {
        asyncListDiffer.submitList(newList)
    }

    fun getItemSelected(): Playlist = asyncListDiffer.currentList.get(0)

    private val asyncListDiffer = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.playlistId == newItem.playlistId
        }

        override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.description == newItem.description
        }
    })

    interface Listener {
        fun onPlaylistSelected(playlist: Playlist)
        fun onFavoriteSelected()
    }
}
