package com.jeanpaulo.musiclibrary.core.ui.bottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.core.databinding.OptionItemBinding

class SongOptionsAdapter(
    private val list: List<SongOption>,
    private val listener: (SongOption) -> Unit
) : RecyclerView.Adapter<SongOptionsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding: OptionItemBinding =
            OptionItemBinding.inflate(layoutInflater, parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(list[position], listener)
    }

    override fun getItemCount() = list.size

    class Holder(val binding: OptionItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(searchOption: SongOption, listener: (SongOption) -> Unit) = with(itemView) {
            binding.text.setText(searchOption.desciption)
            binding.icon.setImageResource(searchOption.icon)
            binding.root.setOnClickListener {
                listener(searchOption)
            }
        }
    }
}