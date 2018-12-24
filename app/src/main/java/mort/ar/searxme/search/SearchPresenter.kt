package mort.ar.searxme.search

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import mort.ar.searxme.manager.SearchManager
import mort.ar.searxme.model.SearxResult
import javax.inject.Inject


class SearchPresenter @Inject constructor(
    private val view: SearchContract.SearchView,
    private val searchManager: SearchManager
) : SearchContract.SearchPresenter {

    private val compositeDisposable = CompositeDisposable()


    override fun start() {
    }

    override fun stop() {
        compositeDisposable.clear()
    }

    override fun onSearchResultClick(searchResult: SearxResult) {
    }

    override fun onSearchSuggestionClick(query: String): Boolean {
        executeSearch(query)

        return false
    }

    override fun onHomeButtonClick() = goHome()

    override fun onQueryTextSubmit(query: String?): Boolean {
        view.hideSearchSuggestions()
        query?.let { executeSearch(it) }

        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        when {
            query.isNullOrEmpty() -> view.hideSearchSuggestions()
            else -> searchManager.requestSearchAutoComplete(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { suggestions -> view.updateSearchSuggestions(suggestions.toList()) }
        }


        return false
    }

    private fun executeSearch(searchTerm: String) {
        view.hideKeyboard()
        compositeDisposable += searchManager.getSearchResults(searchTerm)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { response -> view.updateSearchResults(response) },
                { error -> view.showErrorMessage(error.message) }
            )
    }

    private fun goHome(): Boolean {
        view.hideSearchResults()
        view.hideSearchSuggestions()

        return true
    }

}
