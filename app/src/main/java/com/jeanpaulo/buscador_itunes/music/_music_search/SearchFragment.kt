package com.jeanpaulo.buscador_itunes.music._music_search

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.FragMusicSearchBinding
import com.jeanpaulo.buscador_itunes.util.DataSourceException
import com.jeanpaulo.buscador_itunes.music.domain.model.Music
import com.jeanpaulo.buscador_itunes.util.*
import com.jeanpaulo.buscador_itunes.util.FragmentListener
import com.jeanpaulo.buscador_itunes.music.presentation.MusicFragmentArgs
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.lang.ClassCastException
import java.util.concurrent.TimeUnit


/**
 * Display a grid of [Music]s. User can choose to view all, active or completed tasks.
 */
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel> { getViewModelFactory() }

    private val args: MusicFragmentArgs by navArgs()

    private lateinit var viewBinding: FragMusicSearchBinding
    private lateinit var musicListAdapter: MusicListAdapter

    lateinit var listener: SearchFragmentListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragMusicSearchBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewBinding.root
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            else -> super.onOptionsItemSelected(item)
        }

    private lateinit var disposable: Disposable
    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_main, menu)

        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        disposable = createSearchObservable(searchView)

        return super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Set the lifecycle owner to the lifecycle of the view
        viewBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewBinding.refreshLayout, viewBinding.musicList)
        setupNavigation()
        setupFab()
        initState()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as SearchFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("${context.toString()} must implement OnCompleteListener")
        }
    }

    fun setupFab(){
        listener.setFabVisibility(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun initState() {

        //view model variables

        viewModel.musicList?.observe(viewLifecycleOwner, Observer { it: PagedList<Music> ->
            musicListAdapter.submitList(it)
            musicListAdapter.notifyDataSetChanged()
        })

        viewModel.errorLoading.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                viewBinding.txtError.visibility = View.VISIBLE
                viewBinding.txtError.text = showException(exception)
            } else
                viewBinding.txtError.visibility = View.GONE
        })

        //view components

        viewBinding.txtError.setOnClickListener {
            viewModel.refresh()
        }
    }


    private fun setupNavigation() {
        /*viewModel.newTaskEvent.observe(this, EventObserver {
            navigateToAddNewTask()
        })*/
    }

    private fun setupSnackbar() {
        view?.setupSnackbar(this, viewModel.snackbarText, Snackbar.LENGTH_SHORT)
        arguments?.let {
            //viewModel.showEditResultMessage(args.userMessage)
        }
    }

    private fun showFilteringPopUpMenu() {
        /*val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                viewModel.setFiltering(
                    when (it.itemId) {
                        R.id.active -> TasksFilterType.ACTIVE_TASKS
                        R.id.completed -> TasksFilterType.COMPLETED_TASKS
                        else -> TasksFilterType.ALL_TASKS
                    }
                )
                true
            }
            show()
        }*/
    }

    private fun createSearchObservable(searchView: SearchView?): Disposable {
        val observable: Observable<String> = Observable.create { emitter ->

            searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(searchView?.context, "query: $query", Toast.LENGTH_LONG).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { emitter.onNext(newText) }
                    return true
                }
            })

            emitter.setCancellable {
                searchView?.setOnQueryTextListener(null)
            }
        }

        return observable.filter { it.length > 2 }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
            }
            .observeOn(Schedulers.io())
            .map {
                viewModel.filterTextAll.postValue(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun showException(exception: DataSourceException): String {
        return when (exception.knownNetworkError) {
            DataSourceException.Error.NO_INTERNET_EXCEPTION ->
                getString(R.string.no_internet_connection)
            DataSourceException.Error.TIMEOUT_EXCEPTION ->
                getString(R.string.loading_search_music_error)

            else -> {
                exception.toString()
            }
        }
    }

    private fun navigateToAddNewTask() {
        /*val action = TasksFragmentDirections
            .actionTasksFragmentToAddEditTaskFragment(
                null,
                resources.getString(R.string.add_task)
            )
        findNavController().navigate(action)*/
    }

    private fun setupListAdapter() {
        val viewModel = viewBinding.viewmodel
        if (viewModel != null) {

            musicListAdapter =
                MusicListAdapter(
                    viewModel
                ) { view, it ->
                    openMusicDetail(view, it.id!!, it.name!!, it.artworkUrl!!)
                }
            viewBinding.musicList.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            viewBinding.musicList.adapter = musicListAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            viewBinding.musicList.addItemDecoration(itemDecorator);

        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun openMusicDetail(view: View, musicId: Long, musicName: String, artworkUrl: String) {
        listener.openMusicDetailActivity(view, musicId, musicName, artworkUrl)
    }
}

interface SearchFragmentListener : FragmentListener {

    fun openMusicDetailActivity(view: View, musicId: Long, musicName: String, artworkUrl: String)

}
