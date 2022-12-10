package com.jeanpaulo.musiclibrary.favorite.presentation.view

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.showSnackbar
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.favorite.R
import com.jeanpaulo.musiclibrary.favorite.databinding.FavoriteFragmentBinding
import com.jeanpaulo.musiclibrary.favorite.presentation.viewmodel.FavoriteState
import com.jeanpaulo.musiclibrary.favorite.presentation.viewmodel.FavoriteViewModel


/**
 * A placeholder fragment containing a simple view.
 */
class FavoriteFragment : BaseMvvmFragment() {
    private val viewModel: FavoriteViewModel by appViewModel()

    private var _binding: FavoriteFragmentBinding? = null
    private val binding: FavoriteFragmentBinding get() = _binding!!

    private lateinit var listAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupListeners()
        setupWidgets()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupListeners() {
        viewModel.favoriteState.observe(viewLifecycleOwner) {
            when(it){
                FavoriteState.Error -> showSnackBar("Error")
                FavoriteState.Loading -> showSnackBar("Loading")
                FavoriteState.Success -> showSnackBar("Success")
            }
        }

        viewModel.musicList.observe(viewLifecycleOwner) { it: List<Music> ->
            listAdapter.submitList(it)
            listAdapter.notifyDataSetChanged()
        }
    }

    fun setupWidgets() {
        setupListAdapter()
        setupRefreshLayout(binding.refreshLayout, binding.favoriteList)

        binding.txtError.text = getString(R.string.loading_music_detail_error)
        binding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun setupListAdapter() {
        listAdapter =
            FavoriteAdapter { view, music ->
                openMusicDetail(view, music.id ?: 0L, music.name!!, music.artworkUrl!!)
            }
        binding.favoriteList.layoutManager =
            CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.favoriteList.adapter = listAdapter

        /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

        val itemDecorator = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )

        binding.favoriteList.addItemDecoration(itemDecorator)
    }

    //MENU FUNCTIONS

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val music = listAdapter.getItemSelected()

//        when (item.itemId) {
//            R.id.context_action_favorite -> removeMusicFromFavorite(music.id)
//        }
        return super.onContextItemSelected(item)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        else -> super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteList()
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun openMusicDetail(view: View, musicId: Long, musicName: String, artworkUrl: String) {
//        val action = FavoriteFragmentDirections
//        listener.openMusicDetailActivity(view, musicId, musicName, artworkUrl)
    }

    private fun removeMusicFromFavorite(musicId: Long) {
        viewModel.removeMusicFromFavorite(musicId)
    }
}

