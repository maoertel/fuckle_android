package mort.ar.searxme.search

import mort.ar.searxme.model.SearxResponse
import mort.ar.searxme.model.SearxResult
import mort.ar.searxme.presenter.BasePresenter


interface SearchContract {

    interface BaseView {
        fun showErrorMessage(message: String?)
    }

    interface SearchView : BaseView {
        fun clearSearchView()

        fun updateSearchResults(response: SearxResponse)

        fun hideSearchResults()

        fun updateSearchSuggestions(searchSuggestions: List<String>)

        fun hideSearchSuggestions()

        fun hideKeyboard()
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
