package com.jeanpaulo.musiclibrary.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.databinding.SongItemBinding

import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel

class SongListAdapter(
    private val listener: SongListListener
) : RecyclerView.Adapter<SongListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongListHolder =
        SongListHolder(
            SongItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = asyncListDiffer.currentList.size

    override fun onBindViewHolder(holder: SongListHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    override fun onViewRecycled(holder: SongListHolder) {
        holder.onViewRecycled()
        super.onViewRecycled(holder)
    }

    private fun getItem(position: Int): SongUIModel =
        asyncListDiffer.currentList.get(position)

    fun submitList(newList: List<SongUIModel>) {
        asyncListDiffer.submitList(newList)
    }

    fun getList(): List<SongUIModel> = asyncListDiffer.currentList

    private val asyncListDiffer: AsyncListDiffer<SongUIModel> by lazy {
        AsyncListDiffer(this, object : DiffUtil.ItemCallback<SongUIModel>() {
            override fun areItemsTheSame(oldItem: SongUIModel, newItem: SongUIModel): Boolean {
                return oldItem.musicId == newItem.musicId
            }

            override fun areContentsTheSame(oldItem: SongUIModel, newItem: SongUIModel): Boolean {
                return oldItem.musicName == newItem.musicName &&
                        oldItem.artistName == newItem.artistName &&
                        oldItem.collectionName == newItem.collectionName
            }
        })
    }
}
