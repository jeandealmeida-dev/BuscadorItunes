package com.jeanpaulo.musiclibrary.favorite.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showTopSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListAdapter
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListListener
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListSkeleton
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOption
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOptionsBottomSheet
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.favorite.ui.R
import com.jeanpaulo.musiclibrary.favorite.ui.databinding.FavoriteFragmentBinding

class FavoriteFragment : BaseMvvmFragment() {
    private val viewModel by appViewModel<FavoriteViewModel>()

    private var _binding: FavoriteFragmentBinding? = null
    private val binding: FavoriteFragmentBinding get() = requireNotNull(_binding)

    private var skeleton: SongListSkeleton? = null
    private var listAdapter: SongListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FavoriteFragmentBinding.inflate(inflater, container, false).also {
        _binding = it
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        setupToolbar()
        setupListeners()
        setupFab()
        setupAdapter()
        setupSkeleton()

        viewModel.getFavoriteList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Setups

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(binding.toolbar)
            it.supportActionBar?.apply {
                setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupSkeleton() {
        skeleton = SongListSkeleton(binding.listFavorite)
    }

    private fun setupFab() {
        binding.playAllButton.setOnClickListener {
            listAdapter?.let {
                viewModel.playSongList(requireContext(), it.getList())
            }
        }
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
        binding.listFavorite.layoutManager =
            CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.listFavorite.adapter = listAdapter

        /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

        val itemDecorator = DividerItemDecoration(
            requireContext(),
            DividerItemDecoration.VERTICAL
        )

        binding.listFavorite.addItemDecoration(itemDecorator)
    }

    private fun setupListeners() {
        viewModel.favoriteState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FavoriteState.ShowMusicOptions -> {
                    showMusicOptions(state.music)
                }

                is FavoriteState.Removed -> {
                    requireContext().showTopSnackbar(
                        view = binding.root.rootView,
                        text = getString(R.string.favorite_remove_success)
                    )
                }

                is FavoriteState.Wrapper -> {
                    when (val viewState = state.viewState) {
                        ViewState.Loading -> handleLoading()
                        ViewState.Empty -> handleEmpty()
                        ViewState.Error -> handleError()
                        is ViewState.Success -> handleSuccess(viewState.data)
                    }
                }
            }
        }
    }

    // Handle

    private fun handleLoading() {
        binding.txtEmpty.gone()
        binding.txtError.gone()

        skeleton?.showSkeletons()
    }

    private fun handleEmpty() {
        skeleton?.hideSkeletons()
        binding.txtEmpty.visible()
    }

    private fun handleError() {
        skeleton?.hideSkeletons()
        binding.txtError.visible()
    }

    private fun handleSuccess(data: List<SongUIModel>) {
        skeleton?.hideSkeletons()

        listAdapter?.submitList(data)
    }

    // Show Music Options

    private fun showMusicOptions(music: SongUIModel) {
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
}

