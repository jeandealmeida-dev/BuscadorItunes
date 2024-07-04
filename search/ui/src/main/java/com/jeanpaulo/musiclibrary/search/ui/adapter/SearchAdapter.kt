package com.jeanpaulo.musiclibrary.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel
import com.jeanpaulo.musiclibrary.search.ui.databinding.ItemListFooterBinding
import com.jeanpaulo.musiclibrary.search.ui.databinding.SearchMusicItemBinding
import com.jeanpaulo.musiclibrary.search.ui.model.SearchMusicUIModel
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val viewModel: SearchViewModel,
    private val listener: SearchListener
) : PagingDataAdapter<SearchMusicUIModel, RecyclerView.ViewHolder>(MUSIC_COMPARATOR) {

    private var isLoading: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MUSIC_VIEW_TYPE) {
            val binding =
                SearchMusicItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MusicViewHolder(binding)
        } else {
            val binding =
                ItemListFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            holder is MusicViewHolder -> {
                val music = getItem(position)
                if (music != null) {
                    holder.bind(music, listener)
                    holder.itemView.setOnClickListener { listener.onItemPressed(music) }
                    holder.itemView.setOnLongClickListener {
                        listener.onOptionsPressed(music)
                        false
                    }
                }
            }

            holder is FooterViewHolder -> {
                holder.bind(isLoading, viewModel::refresh)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) MUSIC_VIEW_TYPE else FOOTER_VIEW_TYPE
    }


    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && isLoading
    }


    class MusicViewHolder(private val binding: SearchMusicItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: SearchMusicUIModel, listener: SearchListener) {
            binding.itemMusic.apply {
                musicName.text = music.musicName
                artistName.text = music.artistName
                Picasso
                    .with(binding.root.context)
                    .load(music.artworkUrl)
                    .into(artwork)
            }

//            binding.moreButton.apply {
//                listener.onOptionsPressed(music)
//            }
        }
    }

    class FooterViewHolder(private val binding: ItemListFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(isLoading: Boolean, onClick: () -> Unit) {
            itemView.apply {
                binding.progressBar.isVisible = isLoading
                binding.txtError.isVisible = isLoading
            }
            binding.txtError.setOnClickListener { onClick() }
        }
    }

    interface SearchListener {
        fun onItemPressed(music: SearchMusicUIModel)
        fun onOptionsPressed(music: SearchMusicUIModel)
    }

    companion object {
        internal const val MUSIC_VIEW_TYPE = 1
        internal const val FOOTER_VIEW_TYPE = 2

        private val MUSIC_COMPARATOR = object : DiffUtil.ItemCallback<SearchMusicUIModel>() {

            override fun areItemsTheSame(oldItem: SearchMusicUIModel, newItem: SearchMusicUIModel) =
                oldItem.musicId == newItem.musicId

            override fun areContentsTheSame(
                oldItem: SearchMusicUIModel,
                newItem: SearchMusicUIModel
            ) =
                oldItem == newItem
        }
    }
}