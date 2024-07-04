package com.jeanpaulo.musiclibrary.search.ui.bottom_sheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.search.ui.databinding.SearchOptionItemBinding

class SearchOptionAdapter(
    private val list: List<SearchOption>,
    private val listener: (SearchOption) -> Unit
) : RecyclerView.Adapter<SearchOptionAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: SearchOptionItemBinding =
            SearchOptionItemBinding.inflate(layoutInflater, parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], listener)
        holder.itemView.setOnClickListener {
            listener(getItem(holder.adapterPosition))
        }
    }

    override fun getItemCount() = list.size
    fun getItem(position: Int) = list[position]

    class Holder(val binding: SearchOptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchOption: SearchOption, listener: (SearchOption) -> Unit) = with(itemView) {
            binding.text.setText(searchOption.desciption)
            binding.icon.setImageResource(searchOption.icon)
        }
    }
}