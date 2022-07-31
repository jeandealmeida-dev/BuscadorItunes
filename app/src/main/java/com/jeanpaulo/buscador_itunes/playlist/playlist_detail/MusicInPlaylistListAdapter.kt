package com.jeanpaulo.buscador_itunes.playlist.playlist_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.databinding.ItemMusicBinding
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music

class MusicInPlaylistListAdapter(
    private val listener: (Music) -> Unit
) : RecyclerView.Adapter<MusicInPlaylistListAdapter.MusicInPlaylistViewHolder>() {

    var list = listOf<Music>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicInPlaylistViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: ItemMusicBinding =
            ItemMusicBinding.inflate(layoutInflater, parent, false)
        return MusicInPlaylistViewHolder(itemBinding)
    }

    override fun getItemCount() = list.size

    var positionSelected = -1
    override fun onBindViewHolder(holder: MusicInPlaylistViewHolder, position: Int) {
        holder.bind(list[position], listener)
        holder.itemView.setOnLongClickListener {
            positionSelected = holder.adapterPosition
            false
        }
    }

    override fun onViewRecycled(holder: MusicInPlaylistViewHolder) {
        holder.itemView.setOnLongClickListener(null)
        super.onViewRecycled(holder)
    }

    fun submitList(it: List<Music>) {
        list = it
    }

    class MusicInPlaylistViewHolder(val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music, listener: (Music) -> Unit) = with(itemView) {
            binding.music = music
            setOnClickListener { listener(music) }
        }
    }
}
