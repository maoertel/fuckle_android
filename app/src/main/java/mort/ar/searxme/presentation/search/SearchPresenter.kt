package mort.ar.searxme.presentation.search

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.data.DataModule
import mort.ar.searxme.data.model.SearchInstance
import mort.ar.searxme.data.model.SearchResult
import mort.ar.searxme.domain.InsertInstanceUseCase
import mort.ar.searxme.domain.SearchRequestUseCase
import mort.ar.searxme.domain.SearchSuggestionsUseCase
import mort.ar.searxme.presentation.search.Pages.*
import timber.log.Timber
import javax.inject.Inject

enum class Pages { START, SEARCH_RESULTS, WEBVIEW }

private const val EMPTY = ""

class SearchPresenter @Inject constructor(
  private val searchView: SearchContract.SearchView,
  private val searchRequestUseCase: SearchRequestUseCase,
  private val searchSuggestionsUseCase: SearchSuggestionsUseCase,
  private val insertInstanceUseCase: InsertInstanceUseCase,
  private val compositeDisposable: CompositeDisposable
) : SearchContract.SearchPresenter {

  private var currentPage = START

  private var isSuggestionListSubmit = false
  private var isLastClickBeforeQuitApp = false

  override fun start() = with(searchView) {
    initializeWebViewFragment()
    initializeSearchSuggestionsAdapter()
    initializeSearchResultsAdapter()
  }

  override fun stop() = compositeDisposable.dispose()

  override infix fun onSearchResultClick(searchResult: SearchResult) {
    searchView loadUrl searchResult.prettyUrl
    showPage(WEBVIEW)
  }

  override infix fun onSearchSuggestionClick(query: String) {
    isSuggestionListSubmit = true
    searchView setSearchQuery query
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

  override infix fun onQueryTextSubmit(query: String?): Boolean {
    showPage(SEARCH_RESULTS)
    query?.let { executeSearch(it) }

    return false
  }

  override infix fun onQueryTextChange(query: String?): Boolean {
    when {
      isSuggestionListSubmit -> isSuggestionListSubmit = !isSuggestionListSubmit
      query.isNullOrEmpty() -> searchView.hideSearchSuggestions()
      else ->
        searchSuggestionsUseCase.requestSearchAutoComplete(query)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribeBy(
            onSuccess = { suggestions -> searchView updateSearchSuggestions suggestions },
            onError = { throwable -> searchView showMessage throwable.message }
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
        onSuccess = { searchResults ->
          with(searchView) {
            hideProgress()
            this updateSearchResults searchResults
          }
        },
        onError = { throwable ->
          with(searchView) {
            hideProgress()
            this showMessage throwable.message
          }
        }
      )
      .addTo(compositeDisposable)
  }

  override fun handleOnBackPress(): Boolean =
    when (currentPage) {
      SEARCH_RESULTS -> true.also { showPage(START) }
      WEBVIEW -> true.also { if (!searchView.onBackPressedHandledByWebView()) showPage(SEARCH_RESULTS) }
      START ->
        if (isLastClickBeforeQuitApp) false
        else true.also {
          isLastClickBeforeQuitApp = true
          searchView showMessage "Next click finishes the app"
        }
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
      this setSearchQuery EMPTY
      hideKeyboard()
      hideSearchSuggestions()
      hideSearchResults()
      hideWebView()
    }
  }

  private fun showSearchResultPage() = with(searchView) {
    hideKeyboard()
    hideSearchSuggestions()
    hideWebView()
    showSearchResults()
  }

  private fun showWebViewPage() = with(searchView) {
    hideKeyboard()
    hideSearchResults()
    hideSearchSuggestions()
    showWebView()
  }

  init {
    // TODO following stuff is just a temporary workaround until it is possible to create, update, delete instances
    Completable.merge(instanceList.map { insertInstanceUseCase.insert(it) })
      .subscribeOn(Schedulers.io())
      .subscribeBy(
        onComplete = { DataModule.currentHost = instanceList.first { it.favorite }.url },
        onError = Timber::d
      )
      .addTo(compositeDisposable)
  }
}

private val instanceList =
  listOf(
    SearchInstance(
      name = "https://suche.honigdachse.de",
      url = "https://suche.honigdachse.de",
      favorite = false
    ),
    SearchInstance(
      name = "https://searx.be",
      url = "https://searx.be",
      favorite = false
    ),
    SearchInstance(
      name = "https://anonyk.com",
      url = "https://anonyk.com",
      favorite = false
    ),
    SearchInstance(
      name = "https://search.gibberfish.org",
      url = "https://search.gibberfish.org",
      favorite = true
    )
  )