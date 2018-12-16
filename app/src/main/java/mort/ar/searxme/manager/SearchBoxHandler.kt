package mort.ar.searxme.manager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.SearchSuggestionsAdapter

/**
 * Class that handles interactions of the [SearchView] and acts as a pimped [SearchView.OnQueryTextListener].
 */
class SearchBoxHandler(
    private val mSearchManager: SearchManager,
    private val mSearchView: SearchView,
    suggestionsList: RecyclerView,
    context: Context
) : SearchView.OnQueryTextListener {

    private lateinit var mOnListItemClickEmitter: ObservableEmitter<String>
    private lateinit var mQueryTextSubmitEmitter: ObservableEmitter<String>

    private val mOnListItemClickObservable: Observable<String>
    val mQueryTextSubmitObservable: Observable<String>

    private val mSearchSuggestionsAdapter: SearchSuggestionsAdapter
    private var isListSubmit = false

    private val mCompositeDisposable = CompositeDisposable()


    init {
        mQueryTextSubmitObservable = Observable.create { mQueryTextSubmitEmitter = it }
        mOnListItemClickObservable = Observable.create { mOnListItemClickEmitter = it }
    }

    init {
        mCompositeDisposable += mOnListItemClickObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listItemQuery ->
                isListSubmit = true
                mSearchView.setQuery(listItemQuery, false)
                submitQuery(listItemQuery)
            }
    }

    init {
        mSearchSuggestionsAdapter = SearchSuggestionsAdapter(mOnListItemClickEmitter)
        suggestionsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mSearchSuggestionsAdapter
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = submitQuery(query)

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            isListSubmit -> isListSubmit = !isListSubmit
            else -> when {
                query.isNullOrEmpty() -> {
                    mSearchSuggestionsAdapter.mSearchSuggestions = arrayOf()
                    mSearchSuggestionsAdapter.notifyDataSetChanged()
                }
                else -> mSearchManager.getSearchAutoComplete(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { suggestions ->
                        mSearchSuggestionsAdapter.mSearchSuggestions = suggestions
                        mSearchSuggestionsAdapter.notifyDataSetChanged()
                    }
            }
        }

        return false
    }

    private fun submitQuery(query: String?): Boolean {
        mSearchSuggestionsAdapter.mSearchSuggestions = arrayOf()
        mSearchSuggestionsAdapter.notifyDataSetChanged()
        query?.let { mQueryTextSubmitEmitter.onNext(it) }

        return false
    }

}
