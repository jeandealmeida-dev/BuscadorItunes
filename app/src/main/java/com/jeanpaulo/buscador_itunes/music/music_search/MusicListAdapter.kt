package com.jeanpaulo.buscador_itunes.music.music_search

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.ItemMusicBinding
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.Music
import com.jeanpaulo.buscador_itunes.music.mvvm.domain.model.util.NetworkState
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_footer.view.*
import kotlinx.android.synthetic.main.item_music.view.*


class MusicListAdapter(
    private val viewModel: SearchViewModel,
    private val listener: (View, Music) -> Unit
) : PagedListAdapter<Music, RecyclerView.ViewHolder>(NewsDiffCallback) {

    private val DATA_VIEW_TYPE = 1
    private val FOOTER_VIEW_TYPE = 2

    private var state = NetworkState.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) {
            val layoutInflater = LayoutInflater.from(parent.getContext())
            val itemBinding: ItemMusicBinding =
                ItemMusicBinding.inflate(layoutInflater, parent, false)
            MusicViewHolder(itemBinding)
        } else FooterViewHolder.create(
            {
                viewModel.refresh()
            },
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE) {
            val music = getItem(position)
            (holder as MusicViewHolder).bind(music, listener)
            //holder.itemView.setOnClickListener({ listenerFun(music) })
        } else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == NetworkState.LOADING || state == NetworkState.ERROR)
    }

    class MusicViewHolder(val binding: ItemMusicBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(music: Music?, listener: (View, Music) -> Unit) {
            if (music != null) {
                binding.music = music
                Picasso.get().load(music.artworkUrl).into(itemView.img_artwork)

                if (music.id != null)
                    itemView.setOnClickListener { listener(binding.root, music) }
            }
        }
    }

    class FooterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(status: NetworkState?) {
            itemView.progress_bar.visibility =
                if (status == NetworkState.LOADING) VISIBLE else View.INVISIBLE
            itemView.txt_error.visibility =
                if (status == NetworkState.ERROR) VISIBLE else View.INVISIBLE
        }

        companion object {
            fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_list_footer, parent, false)
                view.txt_error.setOnClickListener { retry() }
                return FooterViewHolder(
                    view
                )
            }
        }
    }
}