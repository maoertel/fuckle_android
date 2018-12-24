package mort.ar.searxme.manager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.search.SearchSuggestionsAdapter


class SearchBoxHandler(
    private val searchManager: SearchManager,
    suggestionsList: RecyclerView,
    context: Context
) : SearchView.OnQueryTextListener {

    private lateinit var onListItemClickEmitter: ObservableEmitter<String>
    private lateinit var queryTextSubmitEmitter: ObservableEmitter<String>

    private val onListItemClickObservable: Observable<String>
    val queryTextSubmitObservable: Observable<String>

    private lateinit var searchSuggestionsAdapter: SearchSuggestionsAdapter
    private var isListSubmit = false

    private val compositeDisposable = CompositeDisposable()


    init {
        onListItemClickObservable = Observable.create<String> { onListItemClickEmitter = it }.share()
        queryTextSubmitObservable = Observable.create { queryTextSubmitEmitter = it }
    }

    init {
        compositeDisposable += onListItemClickObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { listItemQuery ->
                isListSubmit = true
                submitQuery(listItemQuery)
            }
    }

    init {
//        searchSuggestionsAdapter = SearchSuggestionsAdapter(onListItemClickEmitter)
//        suggestionsList.apply {
//            layoutManager = LinearLayoutManager(context)
//            adapter = searchSuggestionsAdapter
//        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = submitQuery(query)

    override fun onQueryTextChange(query: String?): Boolean {
//        when {
//            isListSubmit -> isListSubmit = !isListSubmit
//            else -> when {
//                query.isNullOrEmpty() -> {
//                    searchSuggestionsAdapter.mSearchSuggestions = arrayOf()
//                    searchSuggestionsAdapter.notifyDataSetChanged()
//                }
//                else -> searchManager.requestSearchAutoComplete(query)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe { suggestions ->
//                        searchSuggestionsAdapter.mSearchSuggestions = suggestions
//                        searchSuggestionsAdapter.notifyDataSetChanged()
//                    }
//            }
//        }

        return false
    }

    private fun submitQuery(query: String?): Boolean {
//        searchSuggestionsAdapter.mSearchSuggestions = arrayOf()
//        searchSuggestionsAdapter.notifyDataSetChanged()
//        query?.let { queryTextSubmitEmitter?.onNext(it) }

        return false
    }

}
