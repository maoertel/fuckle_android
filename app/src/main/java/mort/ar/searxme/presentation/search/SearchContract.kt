package mort.ar.searxme.presentation.search

import mort.ar.searxme.data.model.SearchResult

interface SearchContract {

  interface BaseView {
    fun showProgress()

    fun hideProgress()

    infix fun showMessage(message: String?)

    fun hideKeyboard()
  }

  interface WebView {
    infix fun loadUrl(url: String)

    fun onBackPressedHandledByWebView(): Boolean
  }

  interface SearchView : BaseView, WebView {
    fun initializeWebViewFragment()

    fun initializeSearchResultsAdapter()

    fun initializeSearchSuggestionsAdapter()

    infix fun setSearchQuery(query: String)

    infix fun updateSearchResults(searchResult: List<SearchResult>)

    fun showSearchResults()

    fun hideSearchResults()

    infix fun updateSearchSuggestions(searchSuggestions: List<String>)

    fun hideSearchSuggestions()

    fun showWebView()

    fun hideWebView()

    fun startSettings()
  }

  interface BasePresenter {
    fun start()

    fun stop()
  }

  interface SearchResultPresenter {
    infix fun onSearchResultClick(searchResult: SearchResult)
  }

  interface SearchSuggestionPresenter {
    infix fun onSearchSuggestionClick(query: String)
  }

  interface SearchPresenter :
    BasePresenter,
    SearchResultPresenter,
    SearchSuggestionPresenter {

    fun onHomeButtonClick(): Boolean

    fun onSettingsButtonClick(): Boolean

    infix fun onQueryTextSubmit(query: String?): Boolean

    infix fun onQueryTextChange(query: String?): Boolean

    fun handleOnBackPress(): Boolean
  }

}