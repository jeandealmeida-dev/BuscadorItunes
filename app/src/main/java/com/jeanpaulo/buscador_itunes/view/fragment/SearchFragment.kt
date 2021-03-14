package com.jeanpaulo.buscador_itunes.view.fragment

import android.content.Intent
import android.opengl.Visibility
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.jeanpaulo.buscador_itunes.R
import com.jeanpaulo.buscador_itunes.databinding.SearchFragBinding
import com.jeanpaulo.buscador_itunes.model.Music
import com.jeanpaulo.buscador_itunes.util.*
import com.jeanpaulo.buscador_itunes.view.activity.CollectionActivity
import com.jeanpaulo.buscador_itunes.view.adapter.MusicListAdapter
import com.jeanpaulo.buscador_itunes.view_model.SearchViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.content_main.*
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * Display a grid of [Music]s. User can choose to view all, active or completed tasks.
 */
class SearchFragment : Fragment() {

    private val viewModel by viewModels<SearchViewModel> { getViewModelFactory() }

    private val args: MusicFragmentArgs by navArgs()

    private lateinit var viewDataBinding: SearchFragBinding
    private lateinit var musicListAdapter: MusicListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = SearchFragBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        setHasOptionsMenu(true)
        return viewDataBinding.root
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
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        setupSnackbar()
        setupListAdapter()
        setupRefreshLayout(viewDataBinding.refreshLayout, viewDataBinding.musicList)
        setupNavigation()
        setupFab()
        initState()
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun initState() {
        //txt_error.setOnClickListener { viewModel.refresh() }
        //viewModel.getState().observe(this, createNetworkStateObserver())

        viewModel.dataLoading.observe(viewLifecycleOwner, Observer {
            progress_bar.visibility = if(it) View.VISIBLE else View.GONE
        })

        viewModel.musicList?.observe(viewLifecycleOwner, Observer { it: PagedList<Music> ->
            musicListAdapter.submitList(it)
            musicListAdapter.notifyDataSetChanged()
        })
    }


    private fun setupNavigation() {
        viewModel.openMusicEvent.observe(viewLifecycleOwner, EventObserver {
            openMusicDetail(it)
        })
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

            val watcher = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Toast.makeText(searchView?.context, "query: $query", Toast.LENGTH_LONG).show()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { emitter.onNext(newText) }
                    return true
                }
            }
            searchView?.setOnQueryTextListener(watcher)

            emitter.setCancellable {
                searchView?.setOnQueryTextListener(null)
            }
        }

        return observable.filter { it.length > 2 }
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                //showProgress()
            }
            .observeOn(Schedulers.io())
            .map {
                viewModel.filterTextAll.postValue(it)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    private fun setupFab() {
        activity?.findViewById<FloatingActionButton>(R.id.add_task_fab)?.let {
            it.setOnClickListener {
                navigateToAddNewTask()
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
        val viewModel = viewDataBinding.viewmodel
        if (viewModel != null) {

            musicListAdapter = MusicListAdapter(viewModel)
            recycler_view.layoutManager =
                CustomLinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            recycler_view.adapter = musicListAdapter

            /*val itemDecorator =
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
            ContextCompat.getDrawable(this, R.drawable.divider)?.let { itemDecorator.setDrawable(it) }*/

            val itemDecorator = DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )

            recycler_view.addItemDecoration(itemDecorator);
            viewDataBinding.musicList.adapter = musicListAdapter
        } else {
            Timber.w("ViewModel not initialized when attempting to set up adapter.")
        }
    }

    private fun openMusicDetail(musicId: Long) {
        val intent = Intent(requireActivity(), CollectionActivity::class.java)
        val b = Bundle()
        b.putLong("collectionId", musicId) //Your id
        intent.putExtras(b)
        startActivity(intent)
    }
}
