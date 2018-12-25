package mort.ar.searxme.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.manager.Searcher
import mort.ar.searxme.model.SearxResult
import mort.ar.searxme.search.Pages.*
import javax.inject.Inject


private enum class Pages { START, SEARCH_RESULTS, WEBVIEW }

private const val EMPTY = ""


class SearchPresenter @Inject constructor(
    private val view: SearchContract.SearchView,
    private val searcher: Searcher
) : SearchContract.SearchPresenter {

    private val compositeDisposable = CompositeDisposable()

    private var currentPage = START

    private var isSuggestionListSubmit = false
    private var isNextClickQuitsApp = false


    override fun start() {
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun onSearchResultClick(searchResult: SearxResult) {
        view.loadUrl(searchResult.prettyUrl)
        showPage(WEBVIEW)
    }

    override fun onSearchSuggestionClick(query: String) {
        isSuggestionListSubmit = true
        view.setSearchQuery(query)
        showPage(SEARCH_RESULTS)
        executeSearch(query)
    }

    override fun onHomeButtonClick(): Boolean {
        showPage(START)
        return false
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        showPage(SEARCH_RESULTS)
        query?.let { executeSearch(it) }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            isSuggestionListSubmit -> isSuggestionListSubmit = !isSuggestionListSubmit
            query.isNullOrEmpty() -> view.hideSearchSuggestions()
            else -> compositeDisposable += searcher.requestSearchAutoComplete(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { suggestions -> view.updateSearchSuggestions(suggestions.toList()) }
        }

        return true
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
                    view.showMessage(error.message)
                }
            )
    }

    override fun onBackPressed(): Boolean {
        var isHandled = true

        when (currentPage) {
            SEARCH_RESULTS -> showPage(START)
            WEBVIEW -> if (!view.webViewOnBackPressed()) showPage(SEARCH_RESULTS)
            START ->
                when {
                    isNextClickQuitsApp -> isHandled = false
                    else -> {
                        isNextClickQuitsApp = true
                        view.showMessage("Next click finishes the app")
                    }
                }
        }

        return isHandled
    }

    private fun showPage(page: Pages) {
        currentPage = page
        when (page) {
            START -> {
                isNextClickQuitsApp = false
                view.setSearchQuery(EMPTY)
                view.hideKeyboard()
                view.hideSearchSuggestions()
                view.hideSearchResults()
                view.hideWebView()
            }
            SEARCH_RESULTS -> {
                view.hideKeyboard()
                view.hideSearchSuggestions()
                view.hideWebView()
                view.showSearchResults()
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
