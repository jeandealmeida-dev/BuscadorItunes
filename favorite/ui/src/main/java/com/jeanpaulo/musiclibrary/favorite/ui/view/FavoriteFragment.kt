package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.setupRefreshLayout
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.favorite.ui.FavoriteState
import com.jeanpaulo.musiclibrary.favorite.ui.FavoriteViewModel
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteFragmentBinding
import com.jeanpaulo.musiclibrary.music.ui.view.MusicDetailActivity


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
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            //reset
            binding.layoutNoFavorite.gone()
            binding.txtFavoriteError.gone()
            binding.txtFavoriteLoading.gone()

            when (state) {
                FavoriteState.Error -> {
                    binding.txtFavoriteError.let {
                        it.visible()
                        it.setOnClickListener {
                            viewModel.refresh()
                        }
                    }
                }
                FavoriteState.Loading -> {
                    binding.txtFavoriteLoading.visible()
                }
                is FavoriteState.Success -> {
                    if(state.musicList.isEmpty()){
                        binding.layoutNoFavorite.visible()
                    } else {
                        listAdapter.submitList(state.musicList)
                    }
                }
                else -> {}
            }
        }
    }

    fun setupWidgets() {
        setupListAdapter()
        setupRefreshLayout(binding.refreshLayout, binding.favoriteList)

    }

    private fun setupListAdapter() {
        listAdapter =
            FavoriteAdapter { view, music ->
                openMusicDetail(view, music.ds_trackId ?: 0L, music.trackName!!, music.artworkUrl!!)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getFavoriteList()
    }

    private fun showSnackBar(string: String) {
        view?.showSnackbar(string, Snackbar.LENGTH_SHORT)
    }

    private fun openMusicDetail(view: View, musicId: Long, musicName: String, artworkUrl: String) {
        val intent = MusicDetailActivity.newInstance(
            context = requireContext(),
            music = SimpleMusicDetailUIModel(
                id = musicId,
                name = musicName,
                artworkUrl = artworkUrl
            ),
            fromRemote = false
        )

        //Animations
//        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//            requireBaseActivity(),
//            Pair<View, String>(
//                view.findViewById(R.id.musicName),
//                MusicDetailActivity.VIEW_NAME_HEADER_TITLE
//            ),
//            Pair<View, String>(
//                view.findViewById(R.id.artwork),
//                MusicDetailActivity.VIEW_NAME_HEADER_IMAGE
//            )
//        )
        ActivityCompat.startActivity(requireContext(), intent, null)
    }
}

