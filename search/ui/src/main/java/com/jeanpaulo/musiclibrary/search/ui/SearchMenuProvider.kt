package com.jeanpaulo.musiclibrary.search.ui

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchMenuProvider(
    val onQueryChanged: (String) -> Unit
) : MenuProvider {

    private var disposable: Disposable? = null

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu)

        val searchItem: MenuItem? = menu.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        disposable = createSearchObservable(searchView)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return true
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

        return observable.filter { it.length > SEARCH_COUNT_CHAR_FILTER }
            .debounce(SEARCH_DEBOUNCE_MS, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                onQueryChanged(it)
            }
            .subscribe({}, {
                Log.d(TAG, it.message!!)
            })
    }

    fun onDestroy() {
        if (disposable?.isDisposed == false) {
            disposable?.dispose()
        }
    }

    companion object {
        const val TAG = "SearchFragment"

        const val SEARCH_DEBOUNCE_MS = 2000L
        const val SEARCH_COUNT_CHAR_FILTER = 2
    }
}