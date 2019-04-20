package mort.ar.searxme.presentation.search

import mort.ar.searxme.data.remotedata.model.SearchResponse
import mort.ar.searxme.data.remotedata.model.SearxResult


interface SearchContract {

    interface BaseView {
        fun showProgress()

        fun hideProgress()

        fun showMessage(message: String?)

        fun hideKeyboard()
    }

    interface WebView {
        fun loadUrl(url: String)

        fun onBackPressedHandledByWebView(): Boolean
    }

    interface SearchView : BaseView, WebView {
        fun initializeWebViewFragment()

        fun initializeSearchResultsAdapter()

        fun initializeSearchSuggestionsAdapter()

        fun setSearchQuery(query: String)

        fun updateSearchResults(response: SearchResponse)

        fun showSearchResults()

        fun hideSearchResults()

        fun updateSearchSuggestions(searchSuggestions: List<String>)

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
        fun onSearchResultClick(searchResult: SearxResult)
    }

    interface SearchSuggestionPresenter {
        fun onSearchSuggestionClick(query: String)
    }

    interface SearchPresenter :
        BasePresenter,
        SearchResultPresenter,
        SearchSuggestionPresenter {

        fun onHomeButtonClick(): Boolean

        fun onSettingsButtonClick(): Boolean

        fun onQueryTextSubmit(query: String?): Boolean

        fun onQueryTextChange(query: String?): Boolean

        fun handleOnBackPress(): Boolean
    }

}