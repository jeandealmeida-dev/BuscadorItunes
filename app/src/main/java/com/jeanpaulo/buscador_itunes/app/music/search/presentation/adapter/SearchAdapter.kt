package com.jeanpaulo.buscador_itunes.app.music.search.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.model.SearchMusicUIModel
import com.jeanpaulo.buscador_itunes.databinding.ItemListFooterBinding
import com.jeanpaulo.buscador_itunes.app.music.search.presentation.viewmodel.SearchViewModel
import com.jeanpaulo.musiclibrary.commons.databinding.ItemMusicBinding
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_list_footer.view.*

class SearchAdapter(
    private val viewModel: SearchViewModel,
    private val listener: (View, SearchMusicUIModel) -> Unit
) : PagingDataAdapter<SearchMusicUIModel, RecyclerView.ViewHolder>(MUSIC_COMPARATOR) {

    private var isLoading : Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == MUSIC_VIEW_TYPE) {
            val binding =
                ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
                    holder.itemView.setOnClickListener { listener(holder.itemView, music) }
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


    class MusicViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(music: SearchMusicUIModel, listener: (View, SearchMusicUIModel) -> Unit) {
            binding.apply {
                musicName.text = music.musicName
                collectionName.text = music.collectionName
                artistName.text = music.artistName
                Picasso
                    .with(binding.root.context)
                    .load(music.artworkUrl)
                    .into(artwork)

                itemView.setOnClickListener { listener(root, music) }
            }
        }
    }

    class FooterViewHolder(private val binding: ItemListFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(isLoading: Boolean, onClick: () -> Unit) {
            itemView.apply {
                progress_bar.visibility = if (isLoading) VISIBLE else View.INVISIBLE
                txt_error.visibility = if (isLoading) VISIBLE else View.INVISIBLE
            }
            binding.txtError.setOnClickListener { onClick() }
        }
    }

    companion object {
        internal const val MUSIC_VIEW_TYPE = 1
        internal const val FOOTER_VIEW_TYPE = 2

        private val MUSIC_COMPARATOR = object : DiffUtil.ItemCallback<SearchMusicUIModel>() {

            override fun areItemsTheSame(oldItem: SearchMusicUIModel, newItem: SearchMusicUIModel) =
                oldItem.musicId == newItem.musicId

            override fun areContentsTheSame(oldItem: SearchMusicUIModel, newItem: SearchMusicUIModel) =
                oldItem == newItem
        }
    }
}