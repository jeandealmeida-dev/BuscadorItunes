class SearchAdapter() : PagedListAdapter<Music, ConcertViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: ConcertViewHolder, position: Int) {
        val concert: Concert? = getItem(position)

        // Note that "concert" is a placeholder if it's null.
        holder.bindTo(concert)
    }

    companion object {
        private val DIFF_CALLBACK = ... // See Implement the diffing callback section.
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Concert>() {
        // The ID property identifies when items are the same.
        override fun areItemsTheSame(oldItem: Concert, newItem: Concert) =
                oldItem.id == newItem.id

        // If you use the "==" operator, make sure that the object implements
        // .equals(). Alternatively, write custom data comparison logic here.
        override fun areContentsTheSame(
                oldItem: Concert, newItem: Concert) = oldItem == newItem
    }
}