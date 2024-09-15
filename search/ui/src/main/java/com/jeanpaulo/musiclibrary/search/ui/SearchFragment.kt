package com.jeanpaulo.musiclibrary.search.ui

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.commons.extensions.ui.gone
import com.jeanpaulo.musiclibrary.commons.extensions.ui.showTopSnackbar
import com.jeanpaulo.musiclibrary.commons.extensions.ui.visible
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOption
import com.jeanpaulo.musiclibrary.core.ui.bottomsheet.SongOptionsBottomSheet
import com.jeanpaulo.musiclibrary.core.ui.model.SongUIModel
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchAdapter
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchLoadStateAdapter
import com.jeanpaulo.musiclibrary.search.ui.databinding.SearchFragmentBinding
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchState
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel


/**
 * Display a grid of [Music]s. User can choose to view all, active or completed tasks.
 */
class SearchFragment : BaseMvvmFragment() {
    val viewModel by appViewModel<SearchViewModel>()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchMenuProvider: SearchMenuProvider

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListeners()
        setupWidgets()
        setupMenu()

        viewModel.init()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SearchFragmentBinding.inflate(inflater, container, false)
        (requireActivity() as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        return binding.root
    }

    private fun setupListAdapter() {
        searchAdapter = SearchAdapter(object : SearchAdapter.SearchListener {
            override fun onItemPressed(music: SearchUIModel) {
                viewModel.playMusic(requireContext(), music)
            }

            override fun onOptionsPressed(music: SearchUIModel) {
                viewModel.options(music)
            }
        })

        searchAdapter.addLoadStateListener { loadState ->
            binding.searchErrorLayout.isVisible = loadState.source.refresh is LoadState.Error
            binding.searchResultList.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1
            ) {
                binding.searchResultList.gone()
                binding.noResultLayout.visible()
            } else {
                binding.noResultLayout.gone()
            }
        }
    }

    fun setupListeners() {
        viewModel.searchingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Success -> {
                    searchAdapter.submitData(viewLifecycleOwner.lifecycle, state.musicList)
                }

                is SearchState.Options -> {
                    SongOptionsBottomSheet.newInstance(
                        state.music.convertToSongUIModel(),
                        listOf(
                            SongOption.ADD_FAVORITE,
                            SongOption.GO_TO_ARTIST
                        ),
                        object : SongOptionsBottomSheet.MusicOptionListener {
                            override fun onOptionSelected(option: SongOption, song: SongUIModel) {
                                onSearchOptionSelected(option, song)
                            }
                        }
                    ).show(parentFragmentManager, SongOptionsBottomSheet.TAG)
                }

                else -> {}
            }
        }
    }

    fun onSearchOptionSelected(option: SongOption, music: SongUIModel) {
        when (option) {
            SongOption.ADD_FAVORITE -> {
                viewModel.addInFavorite(music.musicId)
                requireContext().showTopSnackbar(
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
                requireContext().showTopSnackbar(
                    view = binding.root,
                    text = getString(option.desciption)
                )
            }
        }
    }

    fun setupWidgets() {
        with(binding) {
            //root.setupSnackbar(this@SearchFragment, viewModel.snackbarText, Snackbar.LENGTH_SHORT)

            searchErrorLayout.setOnClickListener { searchAdapter.retry() }

            searchResultList.setHasFixedSize(true)
            searchResultList.itemAnimator = null
            searchResultList.adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = SearchLoadStateAdapter { searchAdapter.retry() },
                footer = SearchLoadStateAdapter { searchAdapter.retry() },
            )
            searchResultList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            searchResultList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        searchMenuProvider.onDestroy()
    }
}
