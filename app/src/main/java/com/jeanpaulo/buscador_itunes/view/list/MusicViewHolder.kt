package com.jeanpaulo.buscador_itunes.view.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.model.Music
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_music.view.*

class MusicViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bind(music: Music?) {
        if (music != null) {
            itemView.txt_news_name.text = music.name
            Picasso.get().load(music.artworkUrl).into(itemView.img_news_banner)
        }
    }

    companion object {
        fun create(parent: ViewGroup): MusicViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_music, parent, false)
            return MusicViewHolder(view)
        }
    }
}