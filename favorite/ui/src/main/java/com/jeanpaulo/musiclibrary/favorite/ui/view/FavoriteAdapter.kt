package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.commons.databinding.ItemMusicBinding
import com.squareup.picasso.Picasso

class FavoriteAdapter(
    private val listener: (View, Music) -> Unit
) : RecyclerView.Adapter<FavoriteAdapter.MusicInPlaylistViewHolder>() {

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

    fun getItemSelected(): Music = list[positionSelected]

    class MusicInPlaylistViewHolder(val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music, listener: (View, Music) -> Unit) = with(itemView) {
            binding.musicName.text = music.trackName
            binding.artistName.text = music.musicArtist?.name ?: "-"
            binding.collectionName.text = music.musicCollection?.name  ?: "-"
            Picasso.with(binding.root.context).load(music.artworkUrl).into(binding.artwork)
            setOnClickListener { listener(itemView, music) }
        }
    }
}
