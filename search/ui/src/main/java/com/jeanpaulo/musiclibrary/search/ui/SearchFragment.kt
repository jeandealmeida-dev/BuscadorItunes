package com.jeanpaulo.musiclibrary.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.addDivider
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showTopSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.ViewState
import com.jeanpaulo.musiclibrary.core.ui.adapter.SongListSkeleton
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOption
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOptionsBottomSheet
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.player.mp.MPService
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchAdapter
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchLoadStateAdapter
import com.jeanpaulo.musiclibrary.search.ui.databinding.SearchFragmentBinding
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel

class SearchFragment : BaseMvvmFragment() {
    val viewModel by appViewModel<SearchViewModel>()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var skeleton: SongListSkeleton? = null
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchMenuProvider: SearchMenuProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = SearchFragmentBinding.inflate(inflater, container, false).also {
        _binding = it
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListeners()
        setupWidgets()
        setupMenu()
        setupSkeleton()

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        searchMenuProvider.onDestroy()
    }

    private fun setupListAdapter() {
        searchAdapter = SearchAdapter(object : SearchAdapter.SearchListener {
            override fun onItemPressed(music: SearchUIModel) {
                val mpSong = music.convertToSong().toMPSong()
                MPService.playSongList(requireContext(), listOf(mpSong))
            }

            override fun onOptionsPressed(music: SearchUIModel) {
                showOptionsBottomSheet(music.convertToSongUIModel())
            }
        })

        searchAdapter.addLoadStateListener { loadState ->
            updateAdapterVisibility(loadState)
        }
    }

    private fun updateAdapterVisibility(loadState: CombinedLoadStates) {
        with(binding) {
            searchErrorLayout.isVisible = loadState.source.refresh is LoadState.Error
            listSearchResult.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1
            ) {
                listSearchResult.gone()
                noResultLayout.visible()
            } else {
                noResultLayout.gone()
            }
        }
    }

    private fun setupListeners() {
        viewModel.searchingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                ViewState.Loading -> handleLoading()
                is ViewState.Success -> handleSuccess(state.data)
                is ViewState.Error -> handleError()
                is ViewState.Empty -> handleEmpty()
            }
        }
    }

    private fun setupSkeleton() {
        skeleton = SongListSkeleton(binding.listSearchResult)
    }

    private fun setupWidgets() {
        with(binding) {
            searchErrorLayout.setOnClickListener { searchAdapter.retry() }
            listSearchResult.adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = SearchLoadStateAdapter { searchAdapter.retry() },
                footer = SearchLoadStateAdapter { searchAdapter.retry() },
            )
            listSearchResult.addDivider()
        }
    }

    private fun setupMenu() {
        searchMenuProvider = SearchMenuProvider {
            viewModel.setCurrentQuery(it)
        }.also { menuProvider ->
            requireActivity()
                .addMenuProvider(
                    menuProvider,
                    viewLifecycleOwner,
                    Lifecycle.State.RESUMED
                )
        }
    }

    // Handle

    private fun handleLoading() {
        skeleton?.showSkeletons()
    }

    private fun handleEmpty() {
        // TODO
    }

    private fun handleError() {
        view?.showSnackbar(getString(R.string.search_error_message), Snackbar.LENGTH_LONG)
        skeleton?.hideSkeletons()
    }

    private fun handleSuccess(pagingData: PagingData<SearchUIModel>) {
        searchAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        skeleton?.hideSkeletons()
    }

    // BottomSheet

    private fun showOptionsBottomSheet(music: SongUIModel) {
        SongOptionsBottomSheet.newInstance(
            music,
            listOf(
                SongOption.ADD_FAVORITE,
                SongOption.GO_TO_ARTIST
            ),
            object : SongOptionsBottomSheet.MusicOptionListener {
                override fun onOptionSelected(option: SongOption, song: SongUIModel) {
                    onSearchOptionSelected(
                        option = option,
                        music = music
                    )
                }
            }
        ).show(parentFragmentManager, SongOptionsBottomSheet.TAG)
    }

    fun onSearchOptionSelected(option: SongOption, music: SongUIModel) {
        when (option) {
            SongOption.ADD_FAVORITE -> {
                viewModel.addInFavorite(music.musicId)
                showSnackBar(
                    view = binding.root,
                    text = getString(R.string.search_add_favorite_success)
                )
            }

            SongOption.GO_TO_ARTIST -> {
                findNavController().navigate(
                    SearchFragmentDirections
                        .actionSearchFragmentToArtistFragment(
                            artistId = music.artistId,
                            artistName = music.artistName
                        )
                )
            }

            else -> {
                showSnackBar(view = binding.root, text = getString(option.desciption))
            }
        }
    }

    private fun showSnackBar(view: View, text: String) {
        requireContext().showTopSnackbar(
            view = view,
            text = text
        )
    }
}
