package mort.ar.searxme.search

import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult
import mort.ar.searxme.presenter.BasePresenter


interface SearchContract {

    interface BaseView {
        fun showProgress()

        fun hideProgress()

        fun showErrorMessage(message: String?)

        fun hideKeyboard()
    }

    interface WebView {
        fun loadUrl(url: String)
    }

    interface SearchView : BaseView, WebView {
        fun clearSearchView()

        fun updateSearchResults(response: SearxResponse)

        fun hideSearchResults()

        fun updateSearchSuggestions(searchSuggestions: List<String>)

        fun hideSearchSuggestions()

        fun showWebView()

        fun hideWebView()
    }

    interface SearchResultPresenter {
        fun onSearchResultClick(searchResult: SearxResult)
    }

    interface SearchSuggestionPresenter {
        fun onSearchSuggestionClick(query: String): Boolean
    }

    interface SearchPresenter :
        BasePresenter,
        SearchResultPresenter,
        SearchSuggestionPresenter {

        fun onHomeButtonClick(): Boolean

        fun onQueryTextSubmit(query: String?): Boolean

        fun onQueryTextChange(query: String?): Boolean
    }

}
