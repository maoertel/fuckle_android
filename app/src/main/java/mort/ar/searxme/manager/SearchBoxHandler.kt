package mort.ar.searxme.manager

import android.support.v7.widget.SearchView
import android.widget.ArrayAdapter
import android.widget.ListView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Class that handles interactions of the [SearchView] and acts as a pimped [SearchView.OnQueryTextListener].
 */
class SearchBoxHandler(
    private val mSearchManager: SearchManager,
    private val mSearchView: SearchView,
    private val mItemsAdapter: ArrayAdapter<String>,
    mListView: ListView
) : SearchView.OnQueryTextListener {

    private var isListSubmit = false

    private lateinit var mQueryTextSubmitEmitter: ObservableEmitter<String>
    val mQueryTextSubmitObservable: Observable<String>

    init {
        mQueryTextSubmitObservable = Observable.create { mQueryTextSubmitEmitter = it }
        mListView.adapter = mItemsAdapter
        mListView.setOnItemClickListener { adapterView, _, position, _ ->
            val query = adapterView.getItemAtPosition(position) as String
            isListSubmit = true
            mSearchView.setQuery(query, false)
            submitQuery(query)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = submitQuery(query)

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            isListSubmit -> isListSubmit = !isListSubmit
            else -> when {
                query.isNullOrEmpty() -> {
                    mItemsAdapter.clear()
                    mItemsAdapter.notifyDataSetChanged()
                }
                else -> mSearchManager.getSearchAutoComplete(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { suggestions ->
                        mItemsAdapter.clear()
                        suggestions.forEach { mItemsAdapter.add(it) }
                        mItemsAdapter.notifyDataSetChanged()
                    }
            }
        }

        return false
    }

    private fun submitQuery(query: String?): Boolean {
        mItemsAdapter.clear()
        mItemsAdapter.notifyDataSetChanged()
        query?.let { mQueryTextSubmitEmitter.onNext(it) }

        return false
    }

}
