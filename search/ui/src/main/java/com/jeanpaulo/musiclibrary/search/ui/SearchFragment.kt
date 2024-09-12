package com.jeanpaulo.musiclibrary.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
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
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchAdapter
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchLoadStateAdapter
import com.jeanpaulo.musiclibrary.search.ui.databinding.FragMusicSearchBinding
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel

class SearchFragment : BaseMvvmFragment() {
    val viewModel by appViewModel<SearchViewModel>()

    private var _binding: FragMusicSearchBinding? = null
    private val binding get() = requireNotNull(_binding)

    private var skeleton: SongListSkeleton? = null
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchMenuProvider: SearchMenuProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragMusicSearchBinding.inflate(inflater, container, false).also {
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
            override fun onItemPressed(music: SongUIModel) {
                viewModel.playMusic(requireContext(), music)
            }

            override fun onOptionsPressed(music: SongUIModel) {
                showOptionsBottomSheet(music)
            }
        })

        searchAdapter.addLoadStateListener { loadState ->
            binding.searchErrorLayout.isVisible = loadState.source.refresh is LoadState.Error
            binding.listSearchResult.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1
            ) {
                binding.listSearchResult.gone()
                binding.noResultLayout.visible()
            } else {
                binding.noResultLayout.gone()
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

    private fun handleSuccess(pagingData: PagingData<SongUIModel>) {
        searchAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        skeleton?.hideSkeletons()
    }

    // BottomSheet

    private fun showOptionsBottomSheet(music: SongUIModel) {
        SongOptionsBottomSheet.newInstance(
            music,
            listOf(
                SongOption.ADD_FAVORITE,
            ),
            object : SongOptionsBottomSheet.MusicOptionListener {
                override fun onOptionSelected(searchOption: SongOption) {
                    onSearchOptionSelected(
                        option = searchOption,
                        music = music
                    )
                }
            }
        ).show(parentFragmentManager, SongOptionsBottomSheet.TAG)
    }

    fun onSearchOptionSelected(option: SongOption, music: SongUIModel) {
        when (option) {
            SongOption.ADD_FAVORITE -> {
                viewModel.addInFavorite(music)
                requireContext().showTopSnackbar(
                    view = binding.root,
                    text = getString(R.string.search_add_favorite_success)
                )
            }

            else -> {
                requireContext().showTopSnackbar(
                    view = binding.root,
                    text = getString(option.desciption)
                )
            }
        }
    }
}
