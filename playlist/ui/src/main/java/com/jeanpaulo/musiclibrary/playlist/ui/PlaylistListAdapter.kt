package com.jeanpaulo.musiclibrary.playlist.ui

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.domain.model.Playlist
import com.jeanpaulo.musiclibrary.playlist.ui.databinding.ItemPlaylistBinding

class PlaylistListAdapter(
    private val listener: (Playlist) -> Unit
) : RecyclerView.Adapter<PlaylistListAdapter.PlaylistViewHolder>() {

    var list = listOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: ItemPlaylistBinding =
            ItemPlaylistBinding.inflate(layoutInflater, parent, false)
        return PlaylistViewHolder(itemBinding)
    }

    override fun getItemCount() = list.size

    var positionSelected = -1
    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(list[position], listener)
        holder.itemView.setOnLongClickListener {
            positionSelected = holder.adapterPosition
            false
        }
    }

    override fun onViewRecycled(holder: PlaylistViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    fun submitList(it: List<Playlist>) {
        list = it
    }

    fun getItem(position: Int) = list[position]
    fun getItemSelected() = list[positionSelected]


    class PlaylistViewHolder(val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(playlist: Playlist, listener: (Playlist) -> Unit) = with(itemView) {
            binding.txtPlaylistName.text = playlist.title
            binding.txtPlaylistDescription.text = playlist.description
            setOnClickListener { listener(playlist) }
            itemView.setOnCreateContextMenuListener { menu, v, menuInfo ->
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
    }
}
