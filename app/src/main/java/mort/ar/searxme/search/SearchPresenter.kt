package mort.ar.searxme.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.model.SearxResult
import javax.inject.Inject


private const val START = "START"
private const val SEARCH_RESULT = "SEARCH_RESULT"
private const val WEBVIEW = "WEBVIEW"


class SearchPresenter @Inject constructor(
    private val view: SearchContract.SearchView,
    private val searcher: Searcher
) : SearchContract.SearchPresenter {

    private val compositeDisposable = CompositeDisposable()


    override fun start() {
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun onSearchResultClick(searchResult: SearxResult) {
        view.loadUrl(searchResult.prettyUrl)
        showPage(WEBVIEW)
    }

    override fun onSearchSuggestionClick(query: String): Boolean {
        // TODO: fill searchViews query text with suggestions textl
        showPage(SEARCH_RESULT)
        executeSearch(query)

        return false
    }

    override fun onHomeButtonClick(): Boolean {
        showPage(START)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        showPage(SEARCH_RESULT)
        query?.let { executeSearch(it) }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            query.isNullOrEmpty() -> view.hideSearchSuggestions()
            else -> searcher.requestSearchAutoComplete(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { suggestions -> view.updateSearchSuggestions(suggestions.toList()) }
        }

        return false
    }

    private fun executeSearch(searchTerm: String) {
        view.showProgress()
        compositeDisposable += searcher.getSearchResults(searchTerm)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response ->
                    view.hideProgress()
                    view.updateSearchResults(response)
                },
                { error ->
                    view.hideProgress()
                    view.showErrorMessage(error.message)
                }
            )
    }

    private fun showPage(page: String) {
        when (page) {
            START -> {
                view.hideKeyboard()
                view.hideSearchResults()
                view.hideSearchSuggestions()
                view.hideWebView()
            }
            SEARCH_RESULT -> {
                view.hideKeyboard()
                view.hideSearchSuggestions()
                view.hideWebView()
            }
            WEBVIEW -> {
                view.hideKeyboard()
                view.hideSearchResults()
                view.hideSearchSuggestions()
                view.showWebView()
            }
        }
    }

}
