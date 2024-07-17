package com.jeanpaulo.musiclibrary.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.databinding.SongItemBinding
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.search.ui.databinding.ItemListFooterBinding
import com.squareup.picasso.Picasso

class SearchAdapter(
    private val listener: SearchListener
) : PagingDataAdapter<SongUIModel, RecyclerView.ViewHolder>(diffItemCallback) {

    private val FOOTER_VIEW_TYPE = 1
    private val MUSIC_VIEW_TYPE = 0

    private var isLoading: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == FOOTER_VIEW_TYPE) {
            FooterViewHolder(
                ItemListFooterBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            MusicViewHolder(
                SongItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER_VIEW_TYPE) {
            (holder as FooterViewHolder).bind(isLoading) {}
        } else if (getItemViewType(position) == MUSIC_VIEW_TYPE) {
            getItem(position)?.let {
                (holder as MusicViewHolder).bind(it, listener)
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


    class MusicViewHolder(private val binding: SongItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: SongUIModel, listener: SearchListener) {
            binding.apply {
                musicName.text = music.musicName
                artistName.text = music.artistName
                Picasso
                    .with(binding.root.context)
                    .load(music.artworkUrl)
                    .into(artwork)

                // Events
                root.setOnClickListener { listener.onItemPressed(music) }
                root.setOnLongClickListener {
                    listener.onOptionsPressed(music)
                    false
                }
            }

            binding.actionButton.setOnClickListener {
                listener.onOptionsPressed(music)
            }
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
        fun onItemPressed(music: SongUIModel)
        fun onOptionsPressed(music: SongUIModel)
    }

    companion object {

        private val diffItemCallback = object : DiffUtil.ItemCallback<SongUIModel>() {

            override fun areItemsTheSame(oldItem: SongUIModel, newItem: SongUIModel) =
                oldItem.musicId == newItem.musicId

            override fun areContentsTheSame(
                oldItem: SongUIModel,
                newItem: SongUIModel
            ) =
                oldItem == newItem
        }
    }
}