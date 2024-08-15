package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showTopSnackbar
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListAdapter
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListListener
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOption
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOptionsBottomSheet
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteFragmentBinding

class FavoriteFragment : BaseMvvmFragment() {
    private val viewModel by appViewModel<FavoriteViewModel>()

    private var _binding: FavoriteFragmentBinding? = null
    private val binding: FavoriteFragmentBinding get() = _binding!!

    private lateinit var listAdapter: SongListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoriteFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        setupToolbar()
        setupListeners()
        setupFab()
        setupAdapter()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavoriteList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupToolbar() {
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    fun setupFab() {
        binding.playAllButton.setOnClickListener {
            val listSong = listAdapter.getList()
            viewModel.playSongList(requireContext(), listSong)
        }
    }

    fun setupListeners() {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            //reset
            //binding.noFavoriteLayout.gone()

            when (state) {
                is FavoriteState.Loaded -> {
                    if (state.musicList.isEmpty()) {
                        //binding.noFavoriteLayout.visible()
                    } else {
                        listAdapter.submitList(state.musicList)
                    }
                }

                is FavoriteState.ShowMusicOptions -> {
                    showMusicOptions(state.music)
                }

                is FavoriteState.Removed -> {
                    state.music
                    requireContext().showTopSnackbar(
                        view = binding.root.rootView,
                        text = getString(R.string.favorite_remove_success)
                    )
                }

                else -> {}
            }
        }
    }

    fun showMusicOptions(music: SongUIModel) {
        SongOptionsBottomSheet.newInstance(
            music,
            listOf(
                SongOption.REMOVE_FAVORITE,
            ),
            object : SongOptionsBottomSheet.MusicOptionListener {
                override fun onOptionSelected(searchOption: SongOption) {
                    when (searchOption) {
                        SongOption.REMOVE_FAVORITE -> {
                            viewModel.remove(music)
                        }

                        else -> {}
                    }
                }
            }
        ).show(parentFragmentManager, SongOptionsBottomSheet.TAG)
    }

    private fun setupAdapter() {
        listAdapter =
            SongListAdapter(object : SongListListener {

                override fun onPressed(music: SongUIModel) {
                    viewModel.playMusic(requireContext(), music)
                }

                override fun onLongPressed(music: SongUIModel) {
                    viewModel.options(music)
                }

                override fun onActionPressed(music: SongUIModel) {
                    viewModel.options(music)
                }

            })
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
}

