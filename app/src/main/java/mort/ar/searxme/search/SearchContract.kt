package mort.ar.searxme.search

import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult


interface SearchContract {

    interface BaseView {
        fun showProgress()

        fun hideProgress()

        fun showMessage(message: String?)

        fun hideKeyboard()
    }

    interface WebView {
        fun loadUrl(url: String)

        fun webViewOnBackPressed(): Boolean
    }

    interface SearchView : BaseView, WebView {
        fun setSearchQuery(query: String)

        fun updateSearchResults(response: SearxResponse)

        fun showSearchResults()

        fun hideSearchResults()

        fun updateSearchSuggestions(searchSuggestions: List<String>)

        fun hideSearchSuggestions()

        fun showWebView()

        fun hideWebView()
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

        fun onQueryTextSubmit(query: String?): Boolean

        fun onQueryTextChange(query: String?): Boolean

        fun onBackPressed(): Boolean
    }

}
