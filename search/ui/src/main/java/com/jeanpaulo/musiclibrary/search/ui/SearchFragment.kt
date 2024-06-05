package com.jeanpaulo.musiclibrary.search.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.util.Pair
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.jeanpaulo.musiclibrary.commons.base.BaseMvvmFragment
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchViewModel
import com.jeanpaulo.musiclibrary.core.domain.model.Music
import com.jeanpaulo.musiclibrary.commons.view.CustomLinearLayoutManager
import com.jeanpaulo.musiclibrary.core.presentation.SimpleMusicDetailUIModel
//--> v1
import com.jeanpaulo.musiclibrary.music.ui.v1.view.MusicDetailActivity
import com.jeanpaulo.musiclibrary.music.ui.v1.view.MusicDetailActivity.Companion.VIEW_NAME_HEADER_IMAGE
import com.jeanpaulo.musiclibrary.music.ui.v1.view.MusicDetailActivity.Companion.VIEW_NAME_HEADER_TITLE
//--> v2
//import com.jeanpaulo.musiclibrary.music.ui.v2.view.MusicDetailActivity
//import com.jeanpaulo.musiclibrary.music.ui.v2.view.MusicDetailActivity.Companion.VIEW_NAME_HEADER_IMAGE
//import com.jeanpaulo.musiclibrary.music.ui.v2.view.MusicDetailActivity.Companion.VIEW_NAME_HEADER_TITLE
import com.jeanpaulo.musiclibrary.search.ui.databinding.FragMusicSearchBinding
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchAdapter
import com.jeanpaulo.musiclibrary.search.ui.adapter.SearchLoadStateAdapter
import com.jeanpaulo.musiclibrary.search.ui.model.mapper.convertToSimpleMusicDetailUIModel
import com.jeanpaulo.musiclibrary.search.ui.viewmodel.SearchState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Display a grid of [Music]s. User can choose to view all, active or completed tasks.
 */
class SearchFragment : BaseMvvmFragment(), MenuProvider {
    val viewModel by appViewModel<SearchViewModel>()

    private var _binding: FragMusicSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var searchAdapter: SearchAdapter

    private lateinit var disposable: Disposable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListAdapter()
        setupListeners()
        setupWidgets()
        setupMenu()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragMusicSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupListAdapter() {
        searchAdapter = SearchAdapter(viewModel) { view, it ->
            viewModel.openMusicDetail(
                view = view,
                music = it.convertToSimpleMusicDetailUIModel()
            )
        }

        searchAdapter.addLoadStateListener { loadState ->
            binding.txtError.isVisible = loadState.source.refresh is LoadState.Error
            binding.musicList.isVisible = loadState.source.refresh is LoadState.NotLoading
            binding.musicList.isVisible = loadState.source.refresh is LoadState.NotLoading

            if (loadState.source.refresh is LoadState.NotLoading &&
                loadState.append.endOfPaginationReached &&
                searchAdapter.itemCount < 1
            ) {
                binding.musicList.isVisible = false
                binding.noTasksLayout.isVisible = true
            } else {
                binding.noTasksLayout.isVisible = false
            }
        }
    }

    fun setupListeners() {
        viewModel.musicList.observe(viewLifecycleOwner) { pagingData ->
            searchAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                SearchState.Success -> {

                }
                is SearchState.OpenDetail -> {
                    startMusicDetailActivity(state.view, state.music)
                }
                else -> {

                }
            }
        }
    }

    fun setupWidgets() {
        binding.apply {
            //root.setupSnackbar(this@SearchFragment, viewModel.snackbarText, Snackbar.LENGTH_SHORT)

            txtError.setOnClickListener { searchAdapter.retry() }

            musicList.setHasFixedSize(true)
            musicList.itemAnimator = null
            musicList.adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = SearchLoadStateAdapter { searchAdapter.retry() },
                footer = SearchLoadStateAdapter { searchAdapter.retry() },
            )
            musicList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            musicList.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupMenu() {
        setHasOptionsMenu(true)
        requireActivity()
            .addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            else -> super.onOptionsItemSelected(menuItem)
        }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    binding.musicList.scrollToPosition(0)
                    viewModel.setCurrentQuery(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
//        inflater.inflate(R.menu.menu_main, menu)
//
//        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
//        searchView = searchItem?.actionView as SearchView
//        disposable = createSearchObservable(searchView)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //viewModel.showEditResultMessage(args.userMessage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    fun startMusicDetailActivity(view: View, music: SimpleMusicDetailUIModel) {
        val intent = MusicDetailActivity.newInstance(
            context = requireContext(),
            music = music,
            fromRemote = true
        )

        //Animations
//        val titleElement = Pair<View, String>(
//            view.findViewById(R.id.musicName),
//            VIEW_NAME_HEADER_TITLE
//        )
//        val imageElement =
//            Pair<View, String>(view.findViewById(R.id.artwork), VIEW_NAME_HEADER_IMAGE)
//
//        //val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(requireBaseActivity(), *arrayOf(titleElement, imageElement))
//        val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
//            requireBaseActivity(),
//            titleElement,
//            imageElement
//        )
        ActivityCompat.startActivity(requireContext(), intent, null)
    }

    private fun createSearchObservable(searchView: SearchView): Disposable {
        val observable: Observable<String> = Observable.create { emitter ->

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(searchView.context, "query: $query", Toast.LENGTH_LONG).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { emitter.onNext(newText) }
                    return true
                }
            })

            emitter.setCancellable {
                searchView.setOnQueryTextListener(null)
            }
        }

        return observable.filter { it.length > 2 }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .map {
                viewModel.setCurrentQuery(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

}
