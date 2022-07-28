package com.jeanpaulo.buscador_itunes.favorite.presentation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.databinding.ItemMusicBinding
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_music.view.*

class MusicInFavoriteAdapter(
    private val listener: (View, Music) -> Unit
) : RecyclerView.Adapter<MusicInFavoriteAdapter.MusicInPlaylistViewHolder>() {

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
            binding.music = music
            Picasso.get().load(music.artworkUrl).into(itemView.img_artwork)
            setOnClickListener { listener(itemView, music) }
        }
    }
}
