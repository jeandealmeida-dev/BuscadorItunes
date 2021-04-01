package com.jeanpaulo.buscador_itunes.view.fragment.playlist_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.ItemPlaylistBinding
import com.jeanpaulo.buscador_itunes.model.Playlist
import kotlinx.android.synthetic.main.item_playlist.view.*

class PlaylistListAdapter(
    private val listener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder>() {

    var list = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        PlaylistViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_playlist, parent, false)
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) =
        holder.bind(list[position], listener)

    fun submitList(it: List<Playlist>) {
        list = it
    }


    class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = ItemPlaylistBinding.bind(itemView)

        fun bind(playlist: Playlist, listener: (Playlist) -> Unit) = with(itemView) {
            binding.playlist = playlist
            setOnClickListener { listener(playlist) }
        }
    }
}
