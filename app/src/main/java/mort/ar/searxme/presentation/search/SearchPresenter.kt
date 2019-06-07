package mort.ar.searxme.presentation.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.remotedata.model.SearxResult
import mort.ar.searxme.domain.SearchRequestUseCase
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import mort.ar.searxme.presentation.search.Pages.*
import javax.inject.Inject

private enum class Pages { START, SEARCH_RESULTS, WEBVIEW }

private const val EMPTY = ""

class SearchPresenter @Inject constructor(
    private val searchView: SearchContract.SearchView,
    private val searchRequestUseCase: SearchRequestUseCase,
    private val searchSuggestionsUseCase: SearchSuggestionsUseCase,
    private val compositeDisposable: CompositeDisposable
) : SearchContract.SearchPresenter {

    private var currentPage = START

    private var isSuggestionListSubmit = false
    private var isLastClickBeforeQuitApp = false


    override fun start() =
        with(searchView) {
            initializeWebViewFragment()
            initializeSearchSuggestionsAdapter()
            initializeSearchResultsAdapter()
        }

    override fun stop() = compositeDisposable.clear()

    override fun onSearchResultClick(searchResult: SearxResult) {
        searchView.loadUrl(searchResult.prettyUrl)
        showPage(WEBVIEW)
    }

    override fun onSearchSuggestionClick(query: String) {
        isSuggestionListSubmit = true
        searchView.setSearchQuery(query)
        showPage(SEARCH_RESULTS)
        executeSearch(query)
    }

    override fun onHomeButtonClick(): Boolean {
        showPage(START)
        return false
    }

    override fun onSettingsButtonClick(): Boolean {
        searchView.startSettings()
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
            query.isNullOrEmpty() -> searchView.hideSearchSuggestions()
            else ->
                searchSuggestionsUseCase.requestSearchAutoComplete(query)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeBy(
                        onSuccess = { suggestions -> searchView.updateSearchSuggestions(suggestions.toList()) },
                        onError = { throwable -> searchView.showMessage(throwable.message) }
                    )
                    .addTo(compositeDisposable)
        }

        return true
    }

    private fun executeSearch(searchTerm: String) {
        searchView.showProgress()
        searchRequestUseCase.requestSearchResults(searchTerm)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { response ->
                    with(searchView) {
                        hideProgress()
                        updateSearchResults(response)
                    }
                },
                onError = { throwable ->
                    with(searchView) {
                        hideProgress()
                        showMessage(throwable.message)
                    }
                }
            )
            .addTo(compositeDisposable)
    }

    override fun handleOnBackPress(): Boolean {
        var isHandled = true

        when (currentPage) {
            SEARCH_RESULTS -> showPage(START)
            WEBVIEW -> if (!searchView.onBackPressedHandledByWebView()) showPage(SEARCH_RESULTS)
            START ->
                if (isLastClickBeforeQuitApp) isHandled = false
                else {
                    isLastClickBeforeQuitApp = true
                    searchView.showMessage("Next click finishes the app")
                }
        }

        return isHandled
    }

    private fun showPage(page: Pages) {
        currentPage = page
        when (page) {
            START -> showStartPage()
            SEARCH_RESULTS -> showSearchResultPage()
            WEBVIEW -> showWebViewPage()
        }
    }

    private fun showStartPage() {
        isLastClickBeforeQuitApp = false
        with(searchView) {
            setSearchQuery(EMPTY)
            hideKeyboard()
            hideSearchSuggestions()
            hideSearchResults()
            hideWebView()
        }
    }

    private fun showSearchResultPage() =
        with(searchView) {
            hideKeyboard()
            hideSearchSuggestions()
            hideWebView()
            showSearchResults()
        }

    private fun showWebViewPage() =
        with(searchView) {
            hideKeyboard()
            hideSearchResults()
            hideSearchSuggestions()
            showWebView()
        }

}